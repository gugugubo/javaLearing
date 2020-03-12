package com.gcb.jvm.classload;

public class classloadTest24 implements Runnable{

    private Thread thread;

    public classloadTest24() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        ClassLoader contextClassLoader = this.thread.getContextClassLoader();

        ClassLoader.getSystemClassLoader();
        this.thread.setContextClassLoader(contextClassLoader);

        System.out.println(contextClassLoader.getClass());

        System.out.println(contextClassLoader.getParent().getClass());
        /**
         * 输出如下：
         * class sun.misc.Launcher$AppClassLoader
         * class sun.misc.Launcher$ExtClassLoader
         *
         * 主线程的上下文类加载器是系统类加载器
         * 查看 Launcher 通过反编译得到的源码，上下文类加载器由线程的创建者提供，如果未设置，则默认为父线程的上下文类加载器
         * （个人认为这里理解不够，还是不太清楚什么时候设置的）
         * 这是在Launcher 类中通过 Thread.currentThread().setContextClassLoader(this.loader); 代码设置的
         */
    }

    public static void main(String[] args) {

        new classloadTest24();


    }
}
