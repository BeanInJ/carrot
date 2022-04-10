package com.vegetables.system.baseServer;

import com.vegetables.annotation.Controller;
import com.vegetables.core.InnerConfig;

/**
 * 获取系统初始化的一些信息
 */
@Controller
public class CarrotController {


    @Controller()
    public String nullUrl() {
        return getCarrot();
    }

    @Controller("/carrot")
    public String getCarrot() {
        String carrotName = (String) InnerConfig.getConfig().get("carrot.name");
        if(carrotName == null) {
            carrotName = "carrot";
        }

        String carrotVersion = (String) InnerConfig.getConfig().get("carrot.version");
        if(carrotVersion == null) {
            carrotVersion = "1.0.0";
        }

        return "服务注册名：" + carrotName + ", 系统版本：" + carrotVersion;
    }

    @Controller("/carrot")
    public String getCarrotTest() {
        return "重复的URL，已被拦截.";
    }

    @Controller("/version")
    public String getCarrotVersionTest() {
        return "重复的URL，已被强制覆盖。";
    }

    @Controller("/version")
    public String getCarrotVersionTest1() {
        return "重复的URL，已被强制覆盖 ";
    }

    @Controller(value = "/version", isCover = true)
    public String getCarrotVersion() {
        String carrotVersion = (String) InnerConfig.getConfig().get("carrot.version");
        if(carrotVersion == null) {
            carrotVersion = "1.0.0";
        }
        return carrotVersion;
    }

    @Controller("/?")
    public String question() {
        return "此接口已被拦截";
    }
}
