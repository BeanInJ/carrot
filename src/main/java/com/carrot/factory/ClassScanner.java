package com.carrot.factory;

import com.carrot.core.step.ClassFactory;
import com.carrot.system.util.PackageUtil;
import com.carrot.system.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClassScanner {
    private static final Logger log = Logger.getGlobal();

    /**
     * 扫描包，得到的class列表
     */
    public static List<Class<?>> getClassList(String packageName){
        List<Class<?>> list = new ArrayList<>();
        if(StringUtils.isBlankOrNull(packageName)) return list;

        // 扫描得到类名列表
        List<String> nameList;
        try {
            nameList = PackageUtil.getClassName(packageName);
        }catch (IOException e){
            e.printStackTrace();
            log.warning("异常包名：" + packageName);
            return list;
        }

        // 循环获取类
        for (String className : nameList) {
            try {
                Class<?> clazz = Class.forName(className);
                list.add(clazz);
            }catch (ClassNotFoundException e){
                log.info("未扫描到的类型："+className);
            }
        }

        return list;
    }
}
