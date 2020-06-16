package com.gdut.codeexample.jdkproxy;

import java.lang.reflect.Proxy;

public class JdkProxyTest {

    /**
     * 测试jdk动态代理
     *
     * @param args
     */
    public static void main(String[] args) {

        MyInterfaceImpl myInterfaceImpl = new MyInterfaceImpl();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(myInterfaceImpl);
        MyInterface proxyInstance = (MyInterface) Proxy.newProxyInstance(MyInterface.class.getClassLoader(), myInterfaceImpl.getClass().getInterfaces(), myInvocationHandler);
        proxyInstance.say();
        proxyInstance.watch();

    }
}
