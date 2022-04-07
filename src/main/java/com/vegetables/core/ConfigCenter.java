package com.vegetables.core;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

public class ConfigCenter {
    // 默认端口 app.port
    public static final int DEFAULT_APP_PORT = 8080;
    // 默认配置文件路径
    public static final String DEFAULT_CONFIG_FILE_PATH = "config.yml";
    // 默认配置文件编码
//    public static final String DEFAULT_CONFIG_FILE_ENCODING = "UTF-8";

    private Map<String,Object> config;

    public Map<String, Object> getConfig() {
        return config;
    }

    // 服务器启动时加载配置文件
    private void init(){
        Yaml yaml = new Yaml();
        try{
            File file = new File(DEFAULT_CONFIG_FILE_PATH);
            FileReader fileReader = new FileReader(file);
            this.config = yaml.load(fileReader);
        } catch (Exception ignored) {}
    }

    // 加载配置文件
    public static ConfigCenter load(){
        ConfigCenter configCenter = new ConfigCenter();
        configCenter.init();
        return configCenter;
    }

    // 获取端口号
    public int getAppPort(){
        Object port = getConfig().get("app.port");
        if(port == null){
            return DEFAULT_APP_PORT;
        }
        return (int)port;
    }

}
