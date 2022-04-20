package com.carrot;

public class Test {
    public static void main(String[] args) {
            String v = "123.*";
        System.out.println(v.replace(".*",""));
        if(v.endsWith(".*")) System.out.println("sssssssssss");
    }
}
