package com.gdut.codeexample.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

    Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }


    // proxy 是代理对象，method可以获取原来目标的方法，arg是原来目标对象方法中的参数
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("我可以在调用之前增加一些代码");

        // method利用了反射，可以调用原来目标对象的方法
        Object invoke = method.invoke(target, args);

        System.out.println("我可以在调用之后增加一些代码");

        return invoke;
    }
}
