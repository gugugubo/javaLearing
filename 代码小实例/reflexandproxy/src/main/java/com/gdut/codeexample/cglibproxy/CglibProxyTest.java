package com.gdut.codeexample.cglibproxy;

import com.gdut.codeexample.jdkproxy.MyInterface;
import com.gdut.codeexample.jdkproxy.MyInterfaceImpl;
import org.springframework.cglib.proxy.Enhancer;

public class CglibProxyTest {
    public static void main(String[] args) {
        MyInterfaceImpl interfaceImpl = new MyInterfaceImpl();
        MyMethodInterceptor myMethodInterceptor = new MyMethodInterceptor(interfaceImpl);
        Enhancer enhancer = new Enhancer();
        //设置代理子对象的父类
        enhancer.setSuperclass(interfaceImpl.getClass());
        //设置回调接口
        enhancer.setCallback(myMethodInterceptor);

        //创建代理对象
        MyInterfaceImpl cglibproxy = (MyInterfaceImpl) enhancer.create();
        // 输出：class com.gdut.codeexample.jdkproxy.MyInterfaceImpl   说明代理是MyInterfaceImpl的子类
        System.out.println(cglibproxy.getClass().getSuperclass());
        cglibproxy.say();
        cglibproxy.watch();
    }
}
