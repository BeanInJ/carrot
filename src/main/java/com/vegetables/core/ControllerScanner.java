package com.vegetables.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControllerScanner {
    private static final String PACKAGE_PREFIX = "src.main.java.";
    public static final String PACKAGE_NAME = "com.vegetables.controller";
    private final List<Class<?>> classes;

    // 扫描指定包下的所有类
    public static void main(String[] args) throws ClassNotFoundException {
        ControllerScanner controllerScanner = new ControllerScanner();
        List<Class<?>> classes = controllerScanner.getClasses();
        System.out.println(classes.toString());
    }
    public ControllerScanner() throws ClassNotFoundException {
        // 获取src路径
//        packagePath = System.getProperty("user.dir") + "\\src\\";
        String filePath = (PACKAGE_PREFIX+PACKAGE_NAME).replace('.', '\\');
        System.out.println(filePath);
        classes = new ArrayList<>();
        fileScanner(new File(filePath));
    }

    private void fileScanner(File file) throws ClassNotFoundException {
        // 5是".java"的长度
//        boolean isJava = file.isFile() && file.getName().lastIndexOf(".java") == file.getName().length() - 5;
        boolean isJava = file.isFile() && file.getName().endsWith(".java");
        if (isJava) {
            System.out.println(file.getName());
            //  file.getAbsolutePath返回绝对路径
//            String filePath = file.getAbsolutePath();
            String filePath = file.getPath();
            // 将路径换成包名
            String qualifiedName = filePath.substring(PACKAGE_PREFIX.length(), filePath.length() - 5).replace('\\', '.');
            System.out.println(qualifiedName);

            // 加入待加载列表
            classes.add(Class.forName(qualifiedName));
            return;
        } else if (file.isDirectory()) {
            for (File f : file.listFiles())
                fileScanner(f);
        }
    }

    /*
     * 得到加载到的类对象的List,返回的是ArrayList
     */
    public List<Class<?>> getClasses() {
        return this.classes;
    }
}
