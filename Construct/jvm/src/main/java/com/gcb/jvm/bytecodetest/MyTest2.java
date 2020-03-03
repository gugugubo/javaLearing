package com.gcb.jvm.bytecodetest;

/**
 * 此类用于练习分析字节码的信息
 */
public class MyTest2 {

    String str = "Welcome";

    private int x = 5;

    public static Integer in = 5;

    public static void main(String[] args) {
        MyTest2 myTest2 = new MyTest2();

        myTest2.setX(8);

        in = 20;

    }

    private void setX(int x) {
        this.x = x;
    }
}
