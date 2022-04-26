package com.easily.system.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描工具类
 */
public class PackageUtil {
    /**
     * 根据class获取包名
     */
    public static String getPackageByClass(Class<?> clazz){
        return clazz.getPackage().toString().split(" ")[1];
    }

    /**
     * 根据包名获取 类全名列表
     */
    public static List<String> getClassName(String packageName) throws IOException {
        return getClassName(packageName, true);
    }

    public static List<String> getClassName(String packageName, boolean childPackage) throws IOException {
        List<String> fileNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> urls = loader.getResources(packagePath);

        while(urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url != null) {
                String type = url.getProtocol();
                if (type.equals("file")) {
                    fileNames.addAll(getClassNameByFile(url.getPath(), childPackage));
                } else if (type.equals("jar")) {
                    fileNames.addAll(getClassNameByJar(url.getPath(), childPackage));
                }
            }
        }

        fileNames.addAll(getClassNameByJars(((URLClassLoader)loader).getURLs(), packagePath, childPackage));
        return fileNames;
    }

    /**
     * 根据文件路径获取
     */
    private static List<String> getClassNameByFile(String filePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for (File childFile : childFiles) {
                if (childFile.isDirectory()) {
                    if (childPackage) {
                        myClassName.addAll(getClassNameByFile(childFile.getPath(), childPackage));
                    }
                } else {
                    String childFilePath = childFile.getPath();
                    if (childFilePath.endsWith(".class")) {
                        childFilePath = childFilePath.replace("\\", "/");
                        childFilePath = childFilePath.substring(childFilePath.indexOf("/classes/") + 9, childFilePath.lastIndexOf("."));
                        childFilePath = childFilePath.replace("/", ".");

                        myClassName.add(childFilePath);
                    }
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);

        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entry = jarFile.entries();

            while(entry.hasMoreElements()) {
                JarEntry jarEntry = entry.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }

                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return myClassName;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     */
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        if (urls != null) {
            for (URL url : urls) {
                String urlPath = url.getPath();
                if (!urlPath.endsWith("classes/")) {
                    String jarPath = urlPath + "!/" + packagePath;
                    myClassName.addAll(getClassNameByJar(jarPath, childPackage));
                }
            }
        }

        return myClassName;
    }

}