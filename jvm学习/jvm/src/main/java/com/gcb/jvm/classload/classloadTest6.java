package com.gcb.jvm.classload;

/**
 * 测试  类的准备阶段和类型的初始化阶段的静态变量的变化
 */
public class classloadTest6 {
    public static void main(String[] arg){
        Singleton singleton = Singleton.getSingleton();
        System.out.println(Singleton.a);
        System.out.println(Singleton.b);
        /**
         * 输出
         * 2
         * 0
         * 因为最开始是自上而下的准备阶段，a的值为0，singleton为null，b的值为0
         * 然后开始是自上而下的类型初始化阶段，b的值会被覆盖为0
         */
    }
}


class Singleton{
    public static int a = 1;

    private static Singleton singleton = new Singleton();

    public Singleton(){
        a ++;
        b ++;
    }
    public static int b = 0 ;

    public static Singleton getSingleton(){
        return singleton;
    }
}