package com.vegetables;

import com.vegetables.label.annotation.Controller;
import com.vegetables.system.aop.active.AOP;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Controller
@AOP("SS")
public class Test {
    public static void main(String[] args) {
//        String s = "Content-Type: application/json\n" +
//                "User-Agent: PostmanRuntime/7.28.3\n" +
//                "Accept: */*\n" +
//                "Postman-Token: cf8d3520-cc32-4e43-a4bf-ba1dc03de38a\n" +
//                "Host: localhost:8081\n" +
//                "Accept-Encoding: gzip, deflate, br\n" +
//                "Connection: keep-alive\n" +
//                "Content-Length: 12\n" +
//                "\n" +
//                "{\"sss\":\"Ss\"}\n" +
//                "requestArray = 2\n";

//        BaseClassFactory baseClassFactory = new BaseClassFactory();
//        List<Class<?>> classes = new ArrayList<>();
//        classes.add(String.class);
//        baseClassFactory.setClasses(classes);
//
//        BaseClassFactory baseClassFactory1 = new BaseClassFactory();
//        List<Class<?>> classes1 = new ArrayList<>();
//        classes1.add(Long.class);
//        baseClassFactory1.setClasses(classes1);
//
//        System.out.println(baseClassFactory.getClasses().hashCode());
//        System.out.println(baseClassFactory1.getClasses().hashCode());


//        ArrayList<Object> objects = new ArrayList<>();
//        objects.remove("ssss");
        Class<?> clazz = Test.class;
        Map<String,Class<? extends Annotation>> classPoolMap = new HashMap<>();
        classPoolMap.put("sss",Controller.class);
        classPoolMap.put("S2",Controller.class);
        classPoolMap.put("S3",AOP.class);

        classPoolMap.entrySet().stream()
                .filter(entry -> clazz.isAnnotationPresent(entry.getValue()))
                .forEach(entry -> System.out.println( entry.getKey()+"； sss:"+entry.getValue()));
    }
}
