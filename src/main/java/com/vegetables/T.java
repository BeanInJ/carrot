package com.vegetables;

public class T {
    public static void main(String[] args) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s7,cv,e,qs");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s1233,cv,e,qs");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s12wffwe33,cv,e,qs");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s12wfwef33,cv,e,qs");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s12wfefw33,cv,e,qs");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s1wfeff233,cv,e,qs");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
            test("ssc,s12wf33,cv,e,qs");
        }
    }).start();

        new Thread(new Runnable() {
        @Override
        public void run() {
            test("ssc,s12fwe33,cv,e,qs");
        }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s1s233,cv,e,qs");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test("ssc,s123s3,cv,e,qs");
            }
        }).start();



    }
    public static void test(String i){
        String[] s = i.split(",");
        System.out.println(s);
        System.out.println(s.length);
        System.out.println(s[1]);
    }
}
