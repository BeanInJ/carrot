package com.easily.controller;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
//import com.easily.factory.controller.ControllerMethodSubstitute;
//import com.easily.factory.controller.RequestAndResponse;
import com.easily.factory.controller.Urls;
import com.easily.label.AddUrls;
import com.easily.label.Controller;
import com.easily.system.baseServer.CarrotSimpleUrls;
import com.easily.system.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AddUrls
public class AddUrlsTest {
    interface ControllerMethod {
        BaseResponse invoke(BaseRequest request,BaseResponse response);
    }

    public void test(Urls urls) {
        urls.add("/test", "test");
    }

    // 测试AnnotationUtils.hasAnnotation工具类
    public void test2() {
        AddUrlsTest addUrlsTest = new AddUrlsTest();
        boolean b = AnnotationUtils.hasAnnotation(addUrlsTest.getClass(), Controller.class);
        if(b){
            System.out.println("s");
        }
    }

    // 测试CarrotSimpleUrls
    public void test3() {
//        CarrotSimpleUrls
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        ControllerMethodSubstitute substitute = new ControllerMethodSubstitute();
//        Method get = substitute.getClass().getMethod("get");
//        get.invoke("sss");

        // 测试能否拿到匿名函数的参数类型列表
        ControllerMethod controllerMethod = (request,response) -> null;
        Method method = controllerMethod.getClass().getMethod("invoke",BaseRequest.class,BaseResponse.class);
        System.out.println(method.getName());
    }
}
