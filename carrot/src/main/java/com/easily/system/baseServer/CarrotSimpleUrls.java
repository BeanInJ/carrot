package com.easily.system.baseServer;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
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
    }

    public void add(BaseRequest request,BaseResponse response){
        response.setBody(request.getHeader());
    }
    public void getById(BaseRequest request,BaseResponse response){
        response.setBody(request.getHeader());
    }
    public void getList(BaseRequest request,BaseResponse response){
        response.setBody(request.getHeader());
    }

    public void testControllerMethod(BaseRequest request,BaseResponse response){
        response.setBody(request.getHeader());
    }
}
