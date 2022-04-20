package com.carrot.factory.pool;

import com.carrot.factory.ClassPool;
import com.carrot.factory.PoolActuator;
import com.carrot.factory.actuator.ControllerActuator;
import com.carrot.factory.label.Controller;
import com.carrot.system.dict.INNER;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * 控制器类池
 */
public class ControllerPool extends ClassPool {
    private static final Logger log = Logger.getGlobal();

    /**
     * 清除控制器中不包含 url映射 的方法
     */
    @Override
    public void trim() {
        Iterator<Class<?>> iterator = this.classes.iterator();

        while(iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            for(Method method:clazz.getMethods()){
                if (method.isAnnotationPresent(Controller.class)) break;
            }
            log.info("移除无效控制器：" + clazz.getName());
            this.classes.remove(clazz);
        }
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Controller.class;
    }

    @Override
    public String getPoolName() {
        return INNER.CONTROLLER_POOl_NAME;
    }

    @Override
    public PoolActuator getActuator() {
        return new ControllerActuator();
    }
}
