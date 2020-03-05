package com.gcb.jvm.classload;

public class classloadTest22 {

    static {
        System.out.println("classloadTest22 is loading");
    }

    public static void main(String[] args) {
        //扩展类加载器只加载jar包，需要把class文件打成jar
        //此列子中将扩展类加载的位置改成了当前的classes目录
            System.out.println(classloadTest22.class.getClassLoader());
            System.out.println(classloadTest22.class.getClassLoader());
    }
}
