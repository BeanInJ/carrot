package com.easily.core;

import com.easily.core.bootstrap.ElementsSingleton;
import com.easily.system.common.Getter;
import com.easily.system.dict.CONFIG;
import com.easily.system.util.PackageUtil;
import com.easily.system.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置中心
 */
public class ConfigCenter extends ElementsSingleton implements Getter<String,Object> {
    private final Map<String,Object> config = new HashMap<>();

    @Override
    public Object get(String key) {
        return config.get(key);
    }

    public Map<String, Object> getAll() {
        return config;
    }

    /**
     * 从resources中获取config.yml
     */
    private boolean initConfigByMain(Class<?> main){
        try{
            Yaml yaml = new Yaml();
            Enumeration<URL> resources = main.getClassLoader().getResources(CONFIG.APP_CONFIG_NAME_VALUE);
            InputStream content = (InputStream) resources.nextElement().getContent();
            config.putAll(yaml.load(content));

            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 从根目录获取config.yml
     */
    private void initConfigByBootPath(){
        try{
            Yaml yaml = new Yaml();
            File file = new File(CONFIG.APP_CONFIG_NAME_VALUE);
            FileReader fileReader = new FileReader(file);
            config.putAll(yaml.load(fileReader));
        }catch (Exception ignored){}
    }

    public void load(Class<?> mainClass){
        // 加载配置文件 （先从resources中获取，再从根目录获取）
        boolean isConfigByMain = initConfigByMain(mainClass);
        if(!isConfigByMain) initConfigByBootPath();

        // 配置日志
        Object logFile = config.get(CONFIG.APP_LOG_PATH);
        if(StringUtils.isBlankOrNull(logFile)){
            config.put(CONFIG.APP_LOG_PATH, CONFIG.APP_LOG_PATH_VALUE);
        }

        // 初始化端口
        this.config.putIfAbsent(CONFIG.APP_PORT, CONFIG.APP_PORT_VALUE);

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
            outPackage = PackageUtil.getPackageByClass(mainClass);
            config.put(CONFIG.APP_START_PACKAGE, outPackage);
        }
    }
}
