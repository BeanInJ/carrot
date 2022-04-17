package com.carrot.system.BaseServer.pool;

import com.carrot.aop.MethodBody;
import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.core.step.factory.AddPool;
import com.carrot.core.step.factory.ClassPoolCore;
import com.carrot.core.step.factory.Pool;
import com.carrot.system.BaseServer.pool.label.AfterController;
import com.carrot.system.BaseServer.pool.label.BeforeController;
import com.carrot.system.BaseServer.pool.label.Filter;
import com.carrot.system.util.HttpUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 过滤器类
 *
 * 同样可以完成“拦截”功能，过滤器和aop的区别：
 * 过略器是url映射到具体的方法之前进行“拦截”，所以过滤器的拦截更快一些；
 * aop是url映射到具体的方法之后进行“拦截”，aop更适合拦截具体的方法；
 *
 * 过略器是有优先级的，aop是无序的。
 */
@AddPool
public class FilterPool implements Pool {
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();
    private static List<FilterBody> beforeMethods = new ArrayList<>();
    private static List<FilterBody> afterMethods = new ArrayList<>();


    @Override
    public void add(Object o) {
        CLASS_POOL_CORE.add((Class<?>)o);
    }

    public static void add(Class<?> clazz) {

        CLASS_POOL_CORE.add(clazz);
    }

    @Override
    public void trim() {
        for(Class<?> clazz:getClasses()){
            try {
                Object cls = clazz.newInstance();
                for (Method method : clazz.getMethods()) {
                    FilterBody filterBody = new FilterBody();
                    MethodBody methodBody = new MethodBody(cls, method, new Object[]{filterBody});
                    filterBody.setMethodBody(methodBody);
                    if (method.isAnnotationPresent(BeforeController.class)) {
                        beforeMethods.add(filterBody);
                    } else if (method.isAnnotationPresent(AfterController.class)) {
                        afterMethods.add(filterBody);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // 预备执行
        forList(beforeMethods, HttpUtils.getTestRequest(), null);
        forList(afterMethods, HttpUtils.getTestRequest(), HttpUtils.getTestResponse());

        if(beforeMethods.size()>0) {
            // 清理后根据优先级排序
            beforeMethods = beforeMethods.stream()
                    .sorted(
                            Comparator.comparing(filterBody -> ((FilterBody) filterBody).getPriority())
                                    .reversed())
                    .collect(Collectors.toList());

        }

        if(afterMethods.size()>0) {
            afterMethods = afterMethods.stream()
                    .sorted(
                            Comparator.comparing(filterBody -> ((FilterBody) filterBody).getPriority())
                                    .reversed())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Filter.class;
    }

    public static List<Class<?>> getClasses() {
        return CLASS_POOL_CORE.getClasses();
    }

    public static BaseResponse beforeController(BaseRequest request){
        return forList(beforeMethods, request, null);
    }

    public static void afterController(BaseRequest request,BaseResponse response){
        forList(beforeMethods, request, response);
    }

    private static BaseResponse forList(List<FilterBody> list, BaseRequest request, BaseResponse response){
        for(FilterBody filterBody :beforeMethods){
            filterBody.setRequest(request);
            Method method = filterBody.getMethodBody().getMethod();
            Object object = filterBody.getMethodBody().getObject();
            try {
                Class<?>[] parameterType = method.getParameterTypes();
                int parameterCount = method.getParameterCount();
                Object[] params = new Object[parameterCount];
                for (int i = 0; i < parameterCount; i++) {
                    if(parameterType[i] == BaseRequest.class){
                        params[i] = request;
                    }else if(parameterType[i] == BaseResponse.class){
                        params[i] = response;
                    }else if(parameterType[i] == FilterBody.class){
                        params[i] = filterBody;
                    }
                }
                response = (BaseResponse) method.invoke(object, params);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return response;
    }
}
