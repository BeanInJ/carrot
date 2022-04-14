package com.vegetables.core;

import com.vegetables.system.dict.ConfigKey;
import com.vegetables.system.dict.ConfigValue;
import com.vegetables.label.method.YouCanChange;
import com.vegetables.util.PackageUtil;
import com.vegetables.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 静态资源类：配置中心
 *
 * 静态资源类的load()方法一般情况下仅在AppLoader中加载
 */
public class InnerConfig implements YouCanChange {
    // 默认端口 app.port
    private static final int DEFAULT_APP_PORT = 8080;
    // 默认配置文件路径
    private static final String DEFAULT_CONFIG_FILE_PATH = "config.yml";

    // 所有配置内容
    private static final Map<String,Object> config = new HashMap<>();

    public static Map<String, Object> getConfig() {
        return config;
    }

    protected static void update(Map<String,Object> newConfig) {
        config.clear();
        config.putAll(newConfig);
    }

    private InnerConfig(){}

    // 加载配置文件
    protected static void load(Class<?> main){

        // 加载配置文件
        Yaml yaml = new Yaml();
        try{
            File file = new File(DEFAULT_CONFIG_FILE_PATH);
            FileReader fileReader = new FileReader(file);
            config.putAll(yaml.load(fileReader));
        } catch (Exception ignored) {
        }

        // 配置日志
        Object logFile = config.get(ConfigKey.APP_LOG_PATH);
        if(StringUtils.isBlankOrNull(logFile)){
            config.put(ConfigKey.APP_LOG_PATH, ConfigValue.APP_LOG_PATH);
        }

        // 配置ServerSocketChannel大小
        Object serverSocketChannelSize = config.get(ConfigKey.APP_CHANNEL_SIZE);
        if(StringUtils.isBlankOrNull(serverSocketChannelSize)){
            config.put(ConfigKey.APP_CHANNEL_SIZE, ConfigValue.APP_CHANNEL_SIZE);
        }else if(serverSocketChannelSize instanceof String){
                config.put(ConfigKey.APP_CHANNEL_SIZE,
                        Integer.parseInt((String)serverSocketChannelSize));
        }


        // 配置内部包
        Object innerPackageObject = config.get(ConfigKey.APP_INNER_PACKAGE);
        if(StringUtils.isBlankOrNull(innerPackageObject)){
            config.put(ConfigKey.APP_INNER_PACKAGE, ConfigValue.APP_INNER_PACKAGE);
        }

        // 配置外部包
        if (!main.getName().equals(App.class.getName())) {
            String outPackage = (String) config.get(ConfigKey.APP_START_PACKAGE);
            if (StringUtils.isBlankOrNull(outPackage)) {
                outPackage = PackageUtil.getPackageByClass(main);
                config.put(ConfigKey.APP_START_PACKAGE, outPackage);
            }
        }else {
            config.put(ConfigKey.APP_START_PACKAGE, "");
        }
    }

    // 获取端口号
    public static int getAppPort(){
        Object port = getConfig().get(ConfigKey.APP_PORT);
        if(port == null){
            return DEFAULT_APP_PORT;
        }
        return (int)port;
    }

    public static void put(String key,Object value){
        config.put(key,value);
    }
    public static Object get(String key){
        return config.get(key);
    }
}
