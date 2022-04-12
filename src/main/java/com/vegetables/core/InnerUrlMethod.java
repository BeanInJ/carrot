package com.vegetables.core;

import com.vegetables.annotation.Controller;
import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;
import com.vegetables.system.aop.Nanny;
import com.vegetables.system.dict.ConfigKey;
import com.vegetables.system.dict.Msg;
import com.vegetables.system.notch.YouCanChange;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * 静态资源类：将路由映射到对应的方法上
 */
public class InnerUrlMethod implements YouCanChange {
    private static final Logger log = Logger.getGlobal();

    // map<url,[类，方法]>
    private static final Map<String, Object[]> urlMap = new HashMap<>();

    private InnerUrlMethod() {}

    /**
     * 执行 url 对应的方法
      */
    public static Object httpToController(BaseRequest request, BaseResponse response){
        // 这个list装控制器中方法的参数
        List<Object> params = new LinkedList<>();

        // 从request中获取url
        String url = request.getUrl();

        // 根据url得到 [类，方法]
        Object[] objects = getMethod(url);

        // objects == 未匹配到 url
        if (objects == null) {
            response.setStatus("404");
            log.info(Msg.ERROR_NOT_FIND_URL + url);
            return null;
        }

        // 得到controller类
        Object clazz = objects[0];
        // 得到controller中的方法
        Method method = (Method) objects[1];

        // 准备AOP切面执行类
        Nanny nanny = new Nanny();

        // 循环url方法的参数类型
        for (Class<?> parameterType : method.getParameterTypes()) {

            if (parameterType.equals(BaseRequest.class)) {
                // 如果是HttpGetter类型参数，则将HttpGetter对象放入params中
                params.add(request);
            } else if (parameterType.equals(BaseResponse.class)) {
                // 如果是HttpSetter类型参数，则将HttpSetter对象放入params中
                params.add(response);
            } else {
                // 目前前端传过来的全部是String类型，还未进行类型转化
                params.add(request.getBody());
            }
        }

        // 将list转化为Object[]
        Object[] param = params.toArray();

        try {
            // controller方法执行时 AOP 介入
            return nanny.invoke(clazz,method,param);
            //  return method.invoke(clazz, param);
        } catch (Exception e) {
            log.info(clazz.getClass().getName() + "." + method.getName() + " 方法执行异常");
            throw e;
        }
    }

    // 从扫描器中，加载url对应的方法
    protected static void load() {
        // 从扫描器中，获取所有的Controller类
        List<Class<?>> classes = InnerScanner.getControllers();
        for (Class<?> clazz : classes) {
            // 获取类名上的url
            Controller controllerInClass = clazz.getAnnotation(Controller.class);
            // 创建对象
            Object newClazz;
            try {
                newClazz = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            String baseUrl = correctUrl(controllerInClass.value());

            // 获取方法上的url
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Controller.class)) {
                    Controller controllerInMethod = method.getAnnotation(Controller.class);
                    String url = correctUrl(controllerInMethod.value());
                    if (!baseUrl.equals("/")) {
                        url = baseUrl + url;
                    }

                    // 不能放入map条件：map中已有，并且不能覆盖
                    if(urlMap.containsKey(url)){
                        if (controllerInMethod.isCover()){
                            // 覆盖
                            Object oldClass = urlMap.get(url)[0];
                            Method oldMethod = (Method) urlMap.get(url)[1];
                            log.info("url: " + url +
                                    Msg.ERROR_URL_DUPLICATE +
                                    oldClass.getClass().getName()+ "." + oldMethod.getName());
                        }else {
                            // 不能覆盖
                            log.info("url: " + url +
                                    Msg.ERROR_URL_DUPLICATE +
                                    clazz.getName()+ "." + method.getName());
                            continue;
                        }
                    }

                    urlMap.put(url, new Object[]{newClazz, method});
                }
            }
        }

        // 打印urlMap
        if(InnerConfig.getConfig().get(ConfigKey.APP_ACTIVE) == null
                || InnerConfig.getConfig().get(ConfigKey.APP_ACTIVE).equals("test")) {

            urlMapPrint();
        }
    }

    /**
     * 更新url映射器
     */
    protected static void update(Map<String, Object[]> newUrlMap) {
        urlMap.clear();
        urlMap.putAll(newUrlMap);
    }

    private static void urlMapPrint() {
        urlMap.forEach((k, v) -> {
            String path = v[1].toString();
            String[] s = path.split("\\.");
            String method = s[s.length - 2] + "." + s[s.length - 1];

            log.info("url: " + k + "  映射方法: " + method);
        });
    }

    // 纠正url
    private static String correctUrl(String url) {

        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        if (url.endsWith("/")) {
            if (url.length() != 1) {
                url = url.substring(0, url.length() - 1);
            }
        }

        return url;
    }

    // 获取url对应的方法
    private static Object[] getMethod(String url) {
        return urlMap.get(url);
    }
}
