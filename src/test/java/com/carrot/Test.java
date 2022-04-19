package com.carrot;

public class Test {
    public static void main(String[] args) {
        boolean b =
                new Test().i1() &&
                new Test().i2() &&
                new Test().i3() &&
                new Test().i4() &&
                new Test().i5();
    }
    private boolean i1(){
        System.out.println("i1");
        return true;
    }
    private boolean i2(){
        System.out.println("i2");
        return true;
    }
    private boolean i3(){
        System.out.println("i3");
        return false;
    }
    private boolean i4(){
        System.out.println("i4");
        return false;
    }
    private boolean i5(){
        System.out.println("i5");
        return true;
    }
}
