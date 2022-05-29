package org.example.user.controller;

import com.easily.label.Controller;

@Controller("/test")
public class TestController {

    @Controller("/hello")
    public String hello(){
        return "Hello Carrot !";
    }
}
