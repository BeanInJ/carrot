package com.easily.factory.controller;

import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.factory.ClassMeta;
import com.easily.factory.Pool;
import com.easily.label.AddUrls;
import com.easily.label.Controller;
import com.easily.label.Prefix;
import com.easily.label.Suffix;
import com.easily.system.dict.MSG;
import com.easily.system.util.UrlUtils;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ControllerPool implements Pool {
    private static final Logger log = Logger.getGlobal();
    private final Map<String, Object[]> urlMap = new HashMap<>();

    @Override
    public void put(ClassMeta classMeta) throws InvocationTargetException, IllegalAccessException {
        if (classMeta.getClazz().isAnnotationPresent(AddUrls.class)) {
            urlsParse(classMeta);
        } else {
            controllerParse(classMeta);
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Controller.class;
    }

    @Override
    public void end() {
        urlMap.forEach((k, v) -> {
            String methodName = ((Method) v[1]).getName();
            String className = v[0].getClass().getName();
            String[] ns = className.split("\\.");
            String name = ns[ns.length - 1] + "." + methodName;

            log.fine("url: " + k + "  映射方法: " + name);
        });
    }

    /**
     * 解析urls
     */
    private void urlsParse(ClassMeta classMeta) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = classMeta.getClazz();
        // 识别urls类
        urlsMethods:
        for (Method method : clazz.getDeclaredMethods()) {
            // 识别urls方法
            if (method.getParameterCount() == 1) {
                Urls urls = new Urls();
                for (Class<?> parameterType : method.getParameterTypes()) {
                    // 判断参数是否是Urls类型
                    if (!parameterType.equals(Urls.class)) continue urlsMethods;
                }

                Object o = classMeta.getNewObject();
                Object[] prams = new Object[]{urls};
                method.invoke(o, prams);
                Map<String, Object[]> oldUrlsMap = urls.getUrls();
                Map<String, Object[]> newUrlsMap = new HashMap<>();

                String urlPrefix = "";
                String urlSuffix = "";

                if (method.isAnnotationPresent(Prefix.class)) {
                    Prefix prefix = method.getAnnotation(Prefix.class);
                    urlPrefix = prefix.value();
                    urlPrefix = UrlUtils.correctUri(urlPrefix);
                }
                if (method.isAnnotationPresent(Suffix.class)) {
                    Suffix suffix = method.getAnnotation(Suffix.class);
                    urlSuffix = suffix.value();
                }

                // 循环invoke
                for (Map.Entry<String, Object[]> entry : oldUrlsMap.entrySet()) {
                    String url = entry.getKey();
                    newUrlsMap.put(urlPrefix + url + urlSuffix, entry.getValue());
                }
                this.urlMap.putAll(newUrlsMap);
            }

        }

    }

    /**
     * 解析controller
     */
    private void controllerParse(ClassMeta classMeta) {
        Class<?> clazz = classMeta.getClazz();
        Controller controllerInClass = clazz.getAnnotation(Controller.class);
        // 创建对象
        Object newClazz = classMeta.getNewObject();

        String baseUrl = UrlUtils.correctUri(controllerInClass.value());
        // 获取方法上的url
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Controller.class)) {
                Controller controllerInMethod = method.getAnnotation(Controller.class);
                String url = UrlUtils.correctUri(controllerInMethod.value());
                if (!baseUrl.equals("/")) {
                    url = baseUrl + url;
                }

                // 不能放入map条件：map中已有，并且不能覆盖
                if (urlMap.containsKey(url)) {
                    if (controllerInMethod.isCover()) {
                        // 覆盖
                        Object oldClass = urlMap.get(url)[0];
                        Method oldMethod = (Method) urlMap.get(url)[1];
                        log.fine("url: " + url +
                                MSG.ERROR_URL_DUPLICATE +
                                oldClass.getClass().getName() + "." + oldMethod.getName());
                    } else {
                        // 不能覆盖
                        log.fine("url: " + url +
                                MSG.ERROR_URL_DUPLICATE +
                                clazz.getName() + "." + method.getName());
                        continue;
                    }
                }
                urlMap.put(url, new Object[]{newClazz, method});
            }
        }
    }

    public Object[] getMethod(String url) {
        if (url.contains("?")) {
            url = url.split("\\?")[0];
        }
        return this.urlMap.get(url);
    }

    /**
     * 组装Controller方法需要的参数
     */
    public Object[] assemblyParams(Method method, Request request, Response response) {
        List<Object> params = new ArrayList<>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            if (parameterType.equals(Request.class)) {
                params.add(request);
            } else if (parameterType.equals(Response.class)) {
                params.add(response);
            } else if (parameterType.equals(Map.class)){
                params.add((new Gson()).fromJson(request.getBody(),Map.class));
            } else if (parameterType.equals(List.class)){
                params.add((new Gson()).fromJson(request.getBody(),List.class));
            } else if (parameterType.equals(String.class)){
                params.add(request.getBody());
            } else {
                params.add((new Gson()).fromJson(request.getBody(),parameterType));
            }
        }
        return params.toArray();
    }

}
