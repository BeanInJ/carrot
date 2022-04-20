package com.carrot.factory.actuator;

import com.carrot.factory.PoolActuator;
import com.carrot.factory.label.Controller;
import com.carrot.system.dict.MSG;
import com.carrot.system.util.UrlUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class ControllerActuator implements PoolActuator {
    private static final Logger log = Logger.getGlobal();

    // map<url,[类，方法]>
    private static final Map<String, Object[]> urlMap = new HashMap<>();

    @Override
    public void parse(Class<?> clazz) {
        // 获取类名上的url
        Controller controllerInClass = clazz.getAnnotation(Controller.class);
        // 创建对象
        Object newClazz;
        try {
            newClazz = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
}
