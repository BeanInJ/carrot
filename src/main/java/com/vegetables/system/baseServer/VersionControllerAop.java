package com.vegetables.system.baseServer;

import com.vegetables.core.InnerConfig;
import com.vegetables.system.aop.active.AOP;
import com.vegetables.system.aop.active.AOPBefore;
import com.vegetables.system.aop.entity.MethodBody;
import com.vegetables.system.dict.ConfigKey;

@AOP("com.vegetables.system.baseServer.CarrotController.getCarrotVersion")
public class VersionControllerAop {

    /**
     * 拦截 ip:8081/version，返回配置文件中的版本号
     */
    @AOPBefore
    public void getCorrectVersion(MethodBody methodBody){

        Object version = InnerConfig.getConfig().get(ConfigKey.APP_VERSION);
        System.out.println(version);
        if(version != null){
            methodBody.setReturnValue(version);
            methodBody.setReturnNow(true);
        }
    }
}
