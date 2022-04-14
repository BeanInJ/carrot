package com.vegetables.core.factory.classPool;

import com.vegetables.core.factory.ClassPoolCore;
import com.vegetables.core.factory.Pool;
import com.vegetables.entity.BaseRequest;
import com.vegetables.entity.BaseResponse;
import com.vegetables.label.annotation.BeforeReturn;
import com.vegetables.label.method.BeforeReturnFunction;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Logger;

public class BeforeReturnPool implements Pool {
    private static final Logger log = Logger.getGlobal();

    private static final ClassPoolCore CLASS_POOL_CORE = new ClassPoolCore();

    public void trim() {
        for(Class<?> clazz:getClasses()){
            if(clazz.isAssignableFrom(BeforeReturnFunction.class)){
                log.info("移除无效后置拦截器：" + clazz.getName());
                CLASS_POOL_CORE.remove(clazz);
            }
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return BeforeReturn.class;
    }

    public static void add(Class<?> clazz) {
        CLASS_POOL_CORE.add(clazz);
    }

    public static List<Class<?>> getClasses() {
        return CLASS_POOL_CORE.getClasses();
    }

    public static void setClasses(List<Class<?>> classes) {
        CLASS_POOL_CORE.setClasses(classes);
    }

    @Override
    public void add(Object o) {
        CLASS_POOL_CORE.add((Class<?>)o);
    }

    /**
     * 执行在控制器方法之后，传给前端之前
     */
    public static void filter(BaseRequest request,BaseResponse response){
        for (Class<?> beforeReturn:getClasses()) {
            try {
                BeforeReturnFunction beforeEnterFunction = (BeforeReturnFunction) beforeReturn.newInstance();
                beforeEnterFunction.beforeReturn(request, response);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
