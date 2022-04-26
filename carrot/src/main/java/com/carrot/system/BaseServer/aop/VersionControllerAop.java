package com.carrot.system.BaseServer.aop;

import com.carrot.aop.MethodBody;
import com.carrot.aop.annotation.AopBefore;
import com.carrot.core.step.ConfigCenter;
import com.carrot.system.BaseServer.pool.label.Aop;
import com.carrot.system.dict.CONFIG;

@Aop("com.carrot.system.BaseServer.controller.CarrotController.getCarrotVersion")
public class VersionControllerAop {

    /**
     * 拦截 /version，返回配置文件中的版本号
     */
    @AopBefore
    public void getCorrectVersion(MethodBody methodBody){

        Object version = ConfigCenter.getConfig().get(CONFIG.APP_VERSION);
        if(version != null){
            methodBody.setReturnValue(version);
            methodBody.setReturnNow(true);
        }
    }
}