package com.gcb.jvm.classload;

import java.util.Objects;

/**
 * 测试类的主动使用（结论：Class.forName("com.gcb.jvm.classload.Chrild12")是对类的主动使用
 * 而systemClassLoader.loadClass不是对类的主动使用，只是属于类型的加载，）
 */
public class classloadTest12 {
    
    public static void main(String[] srgs) throws ClassNotFoundException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Class<?> loadClass = systemClassLoader.loadClass("com.gcb.jvm.classload.Chrild12");
        System.out.println(loadClass);
        System.out.println("-----------");
        Class<?> aClass = Class.forName("com.gcb.jvm.classload.Chrild12");
        System.out.println(aClass);
        /**
         * 输出结果
         * class com.gcb.jvm.classload.Chrild12
         * -----------
         * Chrild12 is loading
         * class com.gcb.jvm.classload.Chrild12
         */
    }
}


class Chrild12{
    static {
        System.out.println("Chrild12 is loading ");
    }
}
