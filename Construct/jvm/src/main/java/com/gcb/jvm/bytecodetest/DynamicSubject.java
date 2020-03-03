package com.gcb.jvm.bytecodetest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicSubject implements InvocationHandler {

    private Object object;

    public DynamicSubject(Object object){
        this.object = object;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before " + method.toString());

        method.invoke(this.object,args);

        System.out.println("after" + method.toString());
        return null;
    }
}
