package com.gdut.codeexample.cglibproxy;

import org.aopalliance.intercept.MethodInvocation;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * MethodInterceptor是 Callback接口 的子类
 */
public class MyMethodInterceptor implements MethodInterceptor {


    private Object target = null;

    public MyMethodInterceptor(Object target) {
        this.target = target;
    }


    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {


        System.out.println("我可以在调用之前增加一些代码");

        // method利用了反射，可以调用原来目标对象的方法
        Object invoke = method.invoke(target, objects);

        System.out.println("我可以在调用之后增加一些代码");

        return invoke;

    }
}
