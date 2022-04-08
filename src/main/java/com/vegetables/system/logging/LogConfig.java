package com.vegetables.system.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * 配置日志
 */
public class LogConfig {
    private static final Logger log = Logger.getGlobal();

    public static void load() {
        try {
            FileHandler fileHandler = new FileHandler("Carrot.log");
            fileHandler.setFormatter(new CarrotLogFormatter());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CarrotLogFormatter());

            log.addHandler(fileHandler);
            log.addHandler(consoleHandler);
            log.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
