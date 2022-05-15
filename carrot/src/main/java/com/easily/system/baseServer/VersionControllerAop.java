package com.easily.system.baseServer;

import com.easily.core.ConfigCenter;
import com.easily.factory.aop.MethodBody;
import com.easily.label.Aop;
import com.easily.label.AopBefore;
import com.easily.system.dict.CONFIG;

@Aop("com.carrot.system.baseServer.CarrotController.getCarrotVersion")
public class VersionControllerAop {

    /**
     * 拦截 /version，返回配置文件中的版本号
     */
    @AopBefore
    public void getCorrectVersion(MethodBody methodBody){

        Object version = CONFIG.APP_VERSION;
        if(version != null){
            methodBody.setReturnValue(version);
            methodBody.setReturnNow(true);
        }
    }
}