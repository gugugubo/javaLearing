package com.gcb.jvm.bytecodetest;

public class RealSubject  implements Subject{
    @Override
    public void request() {
        System.out.println("this is real subject");
    }
}
