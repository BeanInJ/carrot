package com.easily.system.baseServer;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
import com.easily.factory.controller.Urls;
import com.easily.label.AddUrls;
import com.easily.label.Prefix;
import com.easily.label.Suffix;

@AddUrls
public class CarrotSimpleUrls {
    @Prefix("/test")
    @Suffix(".do")
    public void carrotUrls(Urls urls){
        urls.add("/1","1");
        urls.add("/2","2");
        urls.add("/3","3");
        urls.add("/4",this::testControllerMethod);
    }

    public void testControllerMethod(BaseRequest request,BaseResponse response){
        response.setBody(request.getHeader());
    }
}
