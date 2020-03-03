package com.gcb.jvm.bytecodetest;

import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {
        // 设置系统属性，让它生成代理类的class文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        System.out.println(System.getProperties());
        RealSubject realSubject = new RealSubject();
        DynamicSubject ds = new DynamicSubject(realSubject);
        Class<? extends RealSubject> aClass = realSubject.getClass();
        Subject subject = (Subject)Proxy.newProxyInstance(aClass.getClassLoader(),aClass.getInterfaces(), ds);

        subject.request();

        //com.gcb.jvm.bytecodetest.$Proxy0动态代理出来的结果，是由虚拟机自动生成的
        System.out.println(subject.getClass());
        // 父类为java.lang.reflect.Proxy
        System.out.println(subject.getClass().getSuperclass());
        /**
         * 输出结果
         * before public abstract void com.gcb.jvm.bytecodetest.Subject.request()
         * this is real subject
         * afterpublic abstract void com.gcb.jvm.bytecodetest.Subject.request()
         * class com.gcb.jvm.bytecodetest.$Proxy0
         * class java.lang.reflect.Proxy
         *
         * Proxy.newProxyInstance -> getProxyClass0(loader, intfs)-> proxyClassCache.get(loader, interfaces)
         *  -> proxyClassCache对象是由 proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
         *  构建的，如果给定加载程序定义的代理类实现，给定的接口存在，这只会返回缓存的副本；否则，它将通过ProxyClassFactory创建代理类。
         *  -> ProxyClassFactory -> ProxyGenerator.generateProxyClass(proxyName, interfaces, accessFlags);
         *  可以发现，动态代理就是根据一些必要的条件动态生成一个class文件，
         *  并且根据sun.misc.ProxyGenerator.saveGeneratedFiles系统属性是否为true来选择是否保存此class文件
         *  生成的class文件通过 final Constructor<?> cons = cl.getConstructor(constructorParams);
         *  和cons.newInstance(new Object[]{h});（这里将h【即我们实现了InvocationHandler的类】传了进去）生成实际的对象。
         *  查看动态创建的class文件，可以看到它实际都是通过     进行调用实际的方法的。
         */

    }
}
