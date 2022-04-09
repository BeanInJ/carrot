package com.vegetables.controller.asd;

import com.vegetables.annotation.Controller;
import com.vegetables.entity.HttpGetter;

@Controller("test")
public class TestController2 {

    @Controller("/test2")
    public String test(HttpGetter getter) {
        System.out.println(getter.getUrl());
        System.out.println("test2");
        return "asdksudsdha dhasda";
    }
}
