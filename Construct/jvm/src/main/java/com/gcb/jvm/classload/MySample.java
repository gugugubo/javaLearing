package com.gcb.jvm.classload;


public class MySample {
    public MySample(){
        System.out.println("MySample is loading " + this.getClass().getClassLoader());
        new MyCat();
        System.out.println("MySample is  loading " +MyCat.class);
    }
}
