package com.vegetables.system.baseServer;

import com.vegetables.system.aop.active.AOP;
import com.vegetables.system.aop.active.AOPBefore;

@AOP("")
public class TestAop {
    @AOPBefore
    public void s(){
        System.out.println("");
    }
}
