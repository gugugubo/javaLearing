package com.gcb.jvm.classload;

/**
 * 验证类的初始化顺序
 */
public class classloadTest9 {

    static {
        System.out.println("classloadTest9 is loading ");
    }


    public static void main(String[] args){
        System.out.println(Chirld9.b);
        /**
         * 输出结果为
         * classloadTest9 is loading
         * Chirld9 is loading
         * 9
         * 这是因为仅仅主动使用了Parent10和classloadTest9类
         */
    }
}

class Parent9{
    static int a = 9;

    static {
        System.out.println("Parent9 is loading ");
    }
}

class Chirld9 extends  Parent9{
    static int b = 9;

    static {
        System.out.println("Chirld9 is loading ");
    }
}

