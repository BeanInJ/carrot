package com.carrot.core.step;

import com.carrot.aop.Nanny;
import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.system.BaseServer.pool.ControllerPool;
import com.carrot.system.dict.CONFIG;
import com.carrot.system.dict.MSG;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * url映射器
 *
 * 将请求进来的Url映射到具体的方法上
 * baseRequest --> {url: [method, class]}
 */
public class UrlMapper {
    private static final Logger log = Logger.getGlobal();

    // map<url,[类，方法]>
    private static final Map<String, Object[]> urlMap = new HashMap<>();

    private UrlMapper() {}

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
            log.fine(MSG.ERROR_NOT_FIND_URL + url);
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
    public static void start() {
        ControllerPool.dispense(urlMap);

        // 打印urlMap
        if(ConfigCenter.getConfig().get(CONFIG.APP_ACTIVE) == null
                || ConfigCenter.getConfig().get(CONFIG.APP_ACTIVE).equals("test")) {

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

            log.fine("url: " + k + "  映射方法: " + method);
        });
    }

    // 获取url对应的方法
    private static Object[] getMethod(String url) {
        if(url.contains("?")){
            url = url.split("\\?")[0];
        }
        return urlMap.get(url);
    }

}
