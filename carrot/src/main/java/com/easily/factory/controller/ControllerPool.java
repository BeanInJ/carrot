package com.easily.factory.controller;

import com.easily.factory.ClassPool;
import com.easily.label.Controller;
import com.easily.system.dict.INNER;
import com.easily.system.dict.MSG;
import com.easily.system.util.UrlUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ControllerPool extends ClassPool {
    private static final Logger log = Logger.getGlobal();

    // 产品容器
    private final ControllerContainer container = new ControllerContainer();

    @Override
    public Class<? extends Annotation> getLabel() {
        return Controller.class;
    }

    @Override
    public String getPoolName() {
        return INNER.CONTROLLER_POOl_NAME;
    }

    @Override
    public void parseToContainer() {

        // 移除无控制器方法的类
        for(Class<?> clazz:classes){
            for (Method method : clazz.getDeclaredMethods()) {
                if(method.isAnnotationPresent(Controller.class)){
                    break;
                }
                log.info("移除无效控制器：" + clazz.getName());
                classes.remove(clazz);
            }
        }

        for (Class<?> clazz : classes) {
            Controller controllerInClass = clazz.getAnnotation(Controller.class);
            // 创建对象
            Object newClazz;
            try {
                newClazz = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
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
                    if (container.containsKey(url)) {
                        if (controllerInMethod.isCover()) {
                            // 覆盖
                            Object oldClass = container.getMethod(url)[0];
                            Method oldMethod = (Method) container.getMethod(url)[1];
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
                    container.put(url, new Object[]{newClazz, method});
                }
            }
        }
        container.urlMapPrint();
    }

    /**
     * 获取产品容器
     */
    @Override
    public <T> T getProductContainer(Class<T> clazz) {
        return clazz.isAssignableFrom (ControllerContainer.class)? clazz.cast(this.container):null;
    }

}
