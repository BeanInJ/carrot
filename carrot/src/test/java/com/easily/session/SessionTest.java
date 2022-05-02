package com.easily.session;

import com.easily.cache.Session;

public class SessionTest {
    public static void main(String[] args) {

        // 多线程测试
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                System.out.println(finalI);
                String id2 = Session.put("ssssss"+ finalI);
                System.out.println(id2);
                String str = Session.get(id2,String.class);
                System.out.println(str);
            }).start();
        }
    }
}
