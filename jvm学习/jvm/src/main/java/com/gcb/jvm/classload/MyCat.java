package com.gcb.jvm.classload;

public class MyCat {
    public MyCat(){
        System.out.println("MyCat is loading " + this.getClass().getClassLoader());
    }
}
