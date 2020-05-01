package com.gcb.concurrency1;

/**
 * 在命令行输入命令进行反编译  javap -v  com.gcb.concurrency1.MyTest4
 * 1.当我们使用synchronized关键字来修饰代码块时，
 * 字节码层面上是通过monitorenter与monitorxeit指令来实现的锁的获取与释放动作，
 * 当线程进入到monitorenter指令后，线程将会持有monitor对象，
 * 退出monitorenter指令后，线程将会释放monitor对象。
 */
public class MyTest4 {
    private Object object = new Object();

    public void method(){
        synchronized (object){
            System.out.println("--test--");
        }
    }
}
