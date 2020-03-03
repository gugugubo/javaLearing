package com.gcb.jvm.bytecodetest;


/**
 * cd 到classes 目录下 javap -verbose com.gcb.jvm.bytecodetest.MyTest1
 * 可以得到16进制字节码翻译过来的信息
 */
public class MyTest1 {

    private int a = 1;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

}
