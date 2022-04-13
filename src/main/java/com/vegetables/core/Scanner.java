package com.vegetables.core;

import com.vegetables.core.factory.classPool.ClassFactory;
import com.vegetables.system.dict.ConfigKey;
import com.vegetables.system.dict.ConfigValue;
import com.vegetables.util.PackageUtil;
import com.vegetables.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 扫描器
 */
public class Scanner {
    private static final Logger log = Logger.getGlobal();

    /**
     * 根据注解扫描的包列表
     */
    private static final List<String> classPackage = new ArrayList<>();

    public static void load() {

        // 内部包
        classPackage.add(ConfigValue.APP_INNER_PACKAGE);
        // 外部包
        classPackage.add(InnerConfig.get(ConfigKey.APP_START_PACKAGE).toString());


        startScanner();
    }

    /**
     * 开始扫描包
     */
    private static void startScanner(){
        for (String packageString:classPackage){
            scannerOne(packageString);
        }
        // 清除工厂中不合格产品
        ClassFactory.trim();
    }

    /**
     * 扫描包，并分发扫描得到的class
     */
    private static void scannerOne(String packageName){
        if(StringUtils.isBlankOrNull(packageName)) return;
        List<String> classNames;
        try {
            classNames = PackageUtil.getClassName(packageName);
        }catch (IOException e){
            e.printStackTrace();
            log.warning("异常包名：" + packageName);
            return;
        }

        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                if(aClass.getAnnotations().length != 0) {
                    // ClassFactory 中会判断该类分发到哪个池
                    ClassFactory.addClass(aClass);
                }

            }catch (ClassNotFoundException e){
                log.info("未扫描到的类型："+className);
            }
        }
    }


}
