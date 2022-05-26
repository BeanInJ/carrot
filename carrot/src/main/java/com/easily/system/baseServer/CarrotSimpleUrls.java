package com.easily.system.baseServer;

import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.factory.controller.Urls;
import com.easily.label.AddUrls;
import com.easily.label.Prefix;
import com.easily.label.Suffix;

@AddUrls
public class CarrotSimpleUrls {
    @Prefix("/user")
    @Suffix(".do")
    public void userUrls(Urls urls){
        urls.add("/add",this::add);
        urls.add("/getById",this::getById);
        urls.add("/getList",this::getList);
        urls.add("/save",this::testControllerMethod);
    }

    @Prefix("/carrot")
    public void carrotUrls(Urls urls){
        urls.add("/add",this::add);
        urls.add("/getById",this::getById);
        urls.add("/getList",this::getList);
        urls.add("/save",this::testControllerMethod);
        urls.add("/txt",this::txt);
    }

    public void add(Request request, Response response){
        response.setBody(request.getHeader());
    }
    public void getById(Request request,Response response){
        response.setBody(request.getHeader());
    }
    public void getList(Request request,Response response){
        response.setBody(request.getHeader());
    }

    public void testControllerMethod(Request request,Response response){
        response.setBody(request.getHeader());
    }

    public void txt(Request request,Response response){
        System.out.println("body:");
        System.out.println(request.getBody());
    }
}
