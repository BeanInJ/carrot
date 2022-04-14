package com.vegetables.system.baseServer;

import com.vegetables.entity.BaseResponse;
import com.vegetables.label.annotation.Controller;
import com.vegetables.core.InnerConfig;

/**
 * 获取系统初始化的一些信息
 */
@Controller
public class CarrotController {


    @Controller
    public String nullUrl(BaseResponse response) {
        return getCarrot(response)+", "+getAuthor();
    }

    @Controller(value = "/carrot", isCover = true)
    public String getCarrot(BaseResponse response) {
        String carrotName = (String) InnerConfig.getConfig().get("carrot.name");
        if(carrotName == null) {
            carrotName = "carrot";
        }

        String carrotVersion = (String) InnerConfig.getConfig().get("carrot.version");
        if(carrotVersion == null) {
            carrotVersion = "1.0.0";
        }

        response.setCookie("name","BeanInJ");
        return "服务注册名：" + carrotName + ", 系统版本：" + carrotVersion;
    }

    @Controller(value = "/carrot")
    public String getCarrot1() {
        return "已被强制覆盖";
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

    @Controller(value = "/author", isCover = true)
    public String getAuthor() {
        String author = (String) InnerConfig.getConfig().get("carrot.author");
        if(author == null) {
            author = "Carrot author: BeanInJ";
        }


        return author;
    }

    @Controller(value = "/123", isCover = true)
    public void nullReturn() {
        // 后置拦截器 ResponseIntercept，将拦截空返回
    }
}
