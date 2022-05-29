package org.example.user.service.impl;

import com.easily.label.Service;
import org.example.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    public String getOne(){
        return "zhang:123";
    }
}
