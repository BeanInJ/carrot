package com.easily.system.baseServer.impl;

import com.easily.label.Service;
import com.easily.system.baseServer.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String get() {
        return "Hello Carrot";
    }
}
