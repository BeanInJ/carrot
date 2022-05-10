package com.easily.system.baseServer;

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
    }
}
