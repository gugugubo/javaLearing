package com.gcb.jvm.classload;

/**
 * 验证类的初始化（结论：类只会被初始化一次）
 */
public class classloadTest10 {
    static {
        System.out.println("classloadTest10 is loading ");
    }



    public static void main(String[] args){
        Parent10 parent10 ;
        parent10 = new Parent10();
        // 主动使用
        System.out.println(Parent10.a);
        // 主动使用，但是不会输出静态代码块里面的内容
        System.out.println(Chirld10.b);
    }
}


class Parent10{
    static int a = 9;

    static {
        System.out.println("Parent10 is loading ");
    }
}

class Chirld10 extends  Parent10{
    static int b = 9;

    static {
        System.out.println("Chirld10 is loading ");
    }
}
