package com.easily.system.log;

import com.easily.core.ConfigCenter;
import com.easily.system.dict.CONFIG;
import com.easily.system.util.StringUtils;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 配置日志
 */
public class LogConfig {
    private static final Logger log = Logger.getGlobal();

    public void load(ConfigCenter configCenter) {
        try {
            // 日志输出文件
            String filename = configCenter.get(CONFIG.APP_LOG_PATH,String.class);
            FileHandler fileHandler = new FileHandler(filename);
            fileHandler.setFormatter(new CarrotLogFormatter());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CarrotLogFormatter());

            // 如果没有设置日志等级，就根据 系统环境 判断
            Object logLevel = configCenter.get(CONFIG.APP_LOG_LEVEL);
            if(StringUtils.isBlankOrNull(logLevel)){
                Object dev = configCenter.get(CONFIG.APP_ACTIVE);
                // 测试环境 或 未配置环境，默认打印所有日志
                if(dev == null || "test".equals(dev)  || "dev".equals(dev)){
                    consoleHandler.setLevel(Level.ALL);
                    fileHandler.setLevel(Level.ALL);
                    log.setLevel(Level.ALL);
                }
            }


            log.addHandler(fileHandler);
            log.addHandler(consoleHandler);
            log.setUseParentHandlers(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

