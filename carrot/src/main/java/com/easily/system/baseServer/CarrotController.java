package com.easily.system.baseServer;

import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.label.ConfigValue;
import com.easily.label.Controller;
import com.easily.label.Service;
import com.easily.system.common.CarrotCheck;
import com.easily.system.dict.CONFIG;

import java.util.Map;

/**
 * 获取系统初始化的一些信息
 */
@Controller
public class CarrotController {

    @ConfigValue(CONFIG.APP_NAME)
    String CarrotName;

    @ConfigValue(CONFIG.APP_VERSION)
    String AppVersion;

    @ConfigValue(CONFIG.APP_AUTHOR)
    String AppAuthor;

    @Service
    HelloService service;

    // 测试直接访问端口
    @Controller
    public String nullUrl(Response response) {
        return getCarrot(response)+", "+getAuthor();
    }

    // 测试强制覆盖url
    @Controller(value = "/carrot", isCover = true)
    public String getCarrot(Response response) {
        String carrotName = CarrotName;
        if(carrotName == null) {
            carrotName = "carrot";
        }

        String carrotVersion = AppVersion;
        if(carrotVersion == null) {
            carrotVersion = "1.0.0";
        }

        response.setCookie("name","CarrotAuthor");
        return "服务注册名：" + carrotName + ", 系统版本：" + carrotVersion;
    }

    // 测试强制覆盖url
    @Controller(value = "/carrot")
    public String getCarrot1() {
        return "已被强制覆盖";
    }

    // 测试从配置中心获取数据
    @Controller(value = "/version", isCover = true)
    public String getCarrotVersion() {
        String carrotVersion = AppAuthor;
        if(carrotVersion == null) {
            carrotVersion = "1.0.0";
        }
        return carrotVersion;
    }

    // 测试过滤器拦截
    @Controller("/?")
    public String question() {
        return "此接口已被拦截";
    }

    // 测试正常返回
    @Controller(value = "/author", isCover = true)
    public String getAuthor() {
        String author = AppAuthor;
        if(author == null) {
            author = "Carrot author: CarrotAuthor";
        }
        return author;
    }

    // 测试后置过滤器拦截
    @Controller(value = "/123", isCover = true)
    public void nullReturn() {
        // 后置拦截器 ResponseIntercept，将拦截空返回
    }

    // 测试get请求
    @Controller("/user")
    public String getUrl(Request request, Response response, Map<String,String> map){
        String name = map.get("name");
        response.setBody(request.getUrl()+",username = "+name);
        return null;
    }

    @Controller("/getRequest")
    public String getRequest(Request request, Response response){
        response.setBody(request);
        return null;
    }

    @Controller("/hello")
    public String hello(){
        return service.get();
    }

    @Controller("/nullTest")
    public String nullTest(Map<String,String> map){
        CarrotCheck.cantNull(map,"name","age");
        return "检查通过";
    }
}
