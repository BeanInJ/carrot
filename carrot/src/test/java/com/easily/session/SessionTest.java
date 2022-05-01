package com.easily.session;

import com.easily.cache.Session;

public class SessionTest {
    public static void main(String[] args) {
        Session session = new Session();
        String id = session.put("ssssss");
        Object o = session.get(id);
        String s = session.get(id, String.class);
        System.out.println(s);
    }
}
