package com.carrot.system.util;

import java.lang.reflect.Method;

public class MethodUtils {
    // 执行方法
    public static Object invokeMethod(Object obj, String methodName, Object[] args) {
        try {
            Class<?> clazz = obj.getClass();
            Class<?>[] parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            java.lang.reflect.Method method = clazz.getMethod(methodName, parameterTypes);
            return method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 执行类中 0 参数的方法
    public static Object invokeMethod(Object obj, String methodName) {
        return invokeMethod(obj, methodName, new Object[]{});
    }

    /**
     * 执行Carrot启动前需要执行的代码
     */
    public static void invokeMainMethod(Class<?> clazz) {
        // 执行mainClass中除了main方法以外的其他方法
        Object main;
        try {
            main = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            // 需要提前执行的方法名，必须是beforeStart开头，且不能有参数
            if(name.startsWith("beforeStart") && method.getParameterCount() == 0){
                try {
                    method.invoke(main);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取从类全名中获取包名
     */
    public static String getPackageName(String className) {
        int lastIndex = className.lastIndexOf(".");
        return className.substring(0, lastIndex);
    }

    /**
     * 获取方法全名
     */
    public static String getFullMethodName(Class<?> clazz, String methodName) {
        return clazz.getName() + "." + methodName;
    }

    public static String getFullMethodName(Class<?> clazz, Method method) {
        return clazz.getName() + "." + method.getName();
    }


}
