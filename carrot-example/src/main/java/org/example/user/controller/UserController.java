package org.example.user.controller;

import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.factory.controller.Urls;
import com.easily.label.AddUrls;
import com.easily.label.Prefix;
import com.easily.label.Service;
import org.example.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

@AddUrls
public class UserController {

    @Service
    UserService userService;

    @Prefix("/user")
    public void userUrls(Urls urls){
        urls.add("/getAll",this::getAllUser);
        urls.add("/getOne",this::getOne);
    }
    
    public void getAllUser(Request request, Response response){
        Map<String,String> map = new HashMap<>();
        map.put("zhangsan","123");
        map.put("lisi","456");
        map.put("wangwu","789");
        response.setBody(map);
    }

    public void getOne(Request request, Response response){
        response.setBody(userService.getOne());
    }
}
