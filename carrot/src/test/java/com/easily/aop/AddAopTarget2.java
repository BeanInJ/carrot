package com.easily.aop;

import com.easily.factory.aop.MethodBody;
import com.easily.label.Aop;
import com.easily.label.AopAfter;
import com.easily.label.AopFinally;

/**
 * 第一层包裹
 */
@Aop("com.easily.aop.Target.sout")
public class AddAopTarget2 {
    @AopFinally
    public void aVoid(MethodBody methodBody){
        System.out.println("第三层包裹aop，执行了aop最终方法");
    }
}
