package com.easily.factory.filter;

import com.easily.factory.ClassPool;
import com.easily.factory.aop.MethodBody;
import com.easily.label.AfterController;
import com.easily.label.BeforeController;
import com.easily.label.Filter;
import com.easily.system.dict.INNER;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class FilterPool extends ClassPool {
    // 产品容器
    private final FilterContainer container = new FilterContainer();

    @Override
    public Class<? extends Annotation> getLabel() {
        return Filter.class;
    }

    @Override
    public String getPoolName() {
        return INNER.FILTER_POOl_NAME;
    }

    @Override
    public void parseToContainer() {
        for(Class<?> clazz:classes){
            try {
                Object cls = clazz.newInstance();
                for (Method method : clazz.getMethods()) {
                    FilterBody filterBody = new FilterBody();
                    MethodBody methodBody = new MethodBody(cls, method, new Object[]{filterBody});
                    filterBody.setMethodBody(methodBody);
                    if (method.isAnnotationPresent(BeforeController.class)) {
                        container.addBeforeMethod(filterBody);
                    } else if (method.isAnnotationPresent(AfterController.class)) {
                        container.addAfterMethod(filterBody);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // 排序
        container.order();
    }

    @Override
    public <T> T getProductContainer(Class<T> clazz) {
        if(clazz.isAssignableFrom(FilterContainer.class))
            return (T)container;
        return null;
    }
}
