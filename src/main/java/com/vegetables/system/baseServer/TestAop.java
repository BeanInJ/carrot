package com.vegetables.system.baseServer;

import com.vegetables.system.aop.active.AOP;
import com.vegetables.system.aop.active.AOPBefore;

@AOP("com.vegetables.system.baseServer.CarrotController.getCarrot")
public class TestAop {
    @AOPBefore
    public void s(){
        System.out.println("");
    }
}
