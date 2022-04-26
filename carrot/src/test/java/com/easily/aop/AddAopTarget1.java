package com.easily.aop;

import com.easily.factory.aop.MethodBody;
import com.easily.label.Aop;
import com.easily.label.AopAfter;
import com.easily.label.AopBefore;

/**
 * 第一层包裹
 */
@Aop("com.easily.aop.Target")
public class AddAopTarget1 {
    @AopAfter
    public void aVoid(MethodBody methodBody){
        System.out.println("第二层包裹aop，执行了aop后置方法");
    }
}
