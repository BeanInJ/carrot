package com.vegetables.system.baseServer;

import com.vegetables.annotation.Controller;
import com.vegetables.core.InnerConfig;

@Controller
public class CarrotController {

    @Controller("/")
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

        return "server name is " + carrotName + ", version is " + carrotVersion;
    }

    @Controller("/?")
    public String question() {
        return "此接口已被拦截";
    }
}
