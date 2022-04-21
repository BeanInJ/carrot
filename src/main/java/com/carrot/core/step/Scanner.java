package com.carrot.core.step;

import com.carrot.App;
import com.carrot.system.dict.CONFIG;
import com.carrot.system.util.PackageUtil;
import com.carrot.system.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * class扫描器
 */
public class Scanner {

    private static final Logger log = Logger.getGlobal();

    /**
     * 根据注解扫描的包列表
     */
    private static final List<String> classPackage = new ArrayList<>();

    public static void load() {
        // 内部包
        classPackage.add(CONFIG.APP_INNER_PACKAGE_VALUE);

        // 外部包
        Object outerPackage = ConfigCenter.get(CONFIG.APP_START_PACKAGE);
        if(StringUtils.isNotBlankOrNull(outerPackage)){
            if(!App.class.getName().equals(outerPackage))
                classPackage.add(outerPackage.toString());
        }else {
            log.warning("无法获取main方法所在的包名，您可以在config.yml下手动配置main方法所在的包");
        }
    }

    /**
     * 开始扫描包
     */
    public static void start(){
        for (String packageString:classPackage){
            scannerOne(packageString);
        }
    }

    /**
     * 扫描包，并分发扫描得到的class
     */
    public static void scannerOne(String packageName){
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