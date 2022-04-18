package com.carrot.core.step;

import com.carrot.App;
import com.carrot.system.dict.CONFIG;
import com.carrot.system.util.PackageUtil;
import com.carrot.system.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置中心，这里可以拿到全局配置
 */
public class ConfigCenter {
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

    private ConfigCenter(){}

    // 加载配置文件
    public static void load(Class<?> main){

        // 加载配置文件
        Yaml yaml = new Yaml();
        try{
            File file = new File(DEFAULT_CONFIG_FILE_PATH);
            FileReader fileReader = new FileReader(file);
            config.putAll(yaml.load(fileReader));
        } catch (Exception ignored) {
        }

        // 配置日志
        Object logFile = config.get(CONFIG.APP_LOG_PATH);
        if(StringUtils.isBlankOrNull(logFile)){
            config.put(CONFIG.APP_LOG_PATH, CONFIG.APP_LOG_PATH_VALUE);
        }

        // 配置ServerSocketChannel大小
        Object serverSocketChannelSize = config.get(CONFIG.APP_CHANNEL_SIZE);
        if(StringUtils.isBlankOrNull(serverSocketChannelSize)){
            config.put(CONFIG.APP_CHANNEL_SIZE, CONFIG.APP_CHANNEL_SIZE_VALUE);
        }else if(serverSocketChannelSize instanceof String){
            config.put(CONFIG.APP_CHANNEL_SIZE,
                    Integer.parseInt((String)serverSocketChannelSize));
        }


        // 配置内部包
        Object innerPackageObject = config.get(CONFIG.APP_INNER_PACKAGE);
        if(StringUtils.isBlankOrNull(innerPackageObject)){
            config.put(CONFIG.APP_INNER_PACKAGE, CONFIG.APP_INNER_PACKAGE_VALUE);
        }

        // 配置外部包
        String outPackage = (String) config.get(CONFIG.APP_START_PACKAGE);
        if (StringUtils.isBlankOrNull(outPackage)) {
            outPackage = PackageUtil.getPackageByClass(main);
            config.put(CONFIG.APP_START_PACKAGE, outPackage);
        }
    }

    // 获取端口号
    public static int getAppPort(){
        Object port = getConfig().get(CONFIG.APP_PORT);
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
