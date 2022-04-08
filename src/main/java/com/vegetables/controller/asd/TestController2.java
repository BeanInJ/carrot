package com.vegetables.controller.asd;

import com.vegetables.annotation.Controller;

@Controller("test")
public class TestController2 {

    @Controller("/test2")
    public void test(){
        System.out.println("test2");
    }
}
