package com.carrot.system.log;

import com.carrot.core.step.ConfigCenter;
import com.carrot.system.dict.CONFIG;
import com.carrot.system.util.StringUtils;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 配置日志
 */
public class LogConfig {
    private static final Logger log = Logger.getGlobal();

    public static void load() {
        try {
            // 日志输出文件
            String filename = (String) ConfigCenter.get(CONFIG.APP_LOG_PATH);
            FileHandler fileHandler = new FileHandler(filename);
            fileHandler.setFormatter(new CarrotLogFormatter());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CarrotLogFormatter());

            // 如果没有设置日志等级，就根据 系统环境 判断
            Object logLevel = ConfigCenter.get(CONFIG.APP_LOG_LEVEL);
            if(StringUtils.isBlankOrNull(logLevel)){
                Object dev = ConfigCenter.get(CONFIG.APP_ACTIVE);
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
