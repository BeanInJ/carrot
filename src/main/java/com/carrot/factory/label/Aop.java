package com.carrot.factory.label;

import java.lang.annotation.*;

/**
 * AOP
 * 在前置拦截器后执行
 * 在后置拦截器前执行
 * 如果方法中产生异常，在异常拦截器前执行
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Aop {

    /**
     * value是AOP要扫描的内容
     *
     * <p>
     * value 可能出现的类型：<br>
     * 1、XX.XX                  (匹配具体类，表示该类下的所有controller方法都接收切面) <br>
     * 3、XX.XX*                 (模糊匹配类名) <br>
     * 4、XX.XX.*                (匹配XX.XX包下所有类) <br>
     * 5、XX.*.YY                (匹配XX包下所有子包中名叫YY的类) <br>
     * 6、class:XX.XX            ( = XX.XX) <br>
     * 7、method:XX.XX.xx        (匹配具体方法，仅该方法接收切面) <br>
     * 8、package:包名            (匹配具体包，该包下的所有controller方法都接收切面) <br>
     * 9、method:XX.*.YY.GG*     (匹配XX包下所有子包中名叫YY的类里的GG开头的方法) <br>
     * 10、exception: XX.XXException (匹配异常) <br>
     * </p>
     */
    String value();
}