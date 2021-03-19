//package com.gcb.jvm.memory;
//
//import net.sf.cglib.proxy.Callback;
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//
///**
// * 方法区内存溢出
// * 设置vm 元空间的大小-XX:MaxMetaspaceSize=100m
// *  查看java的[进程 PID]信息cmd下输入 jsp -l
// *
// *  jconsole 和 jvisualvm 可视界面可以查看内存和类相关的信息
// *
// *  jmap -clstats PID 打印类加载器数据。
// *
// *  jstat -gc PID 用来打印元空间的信息
// *
// *
// *  jcmd PID GC.class_stats 一个新的诊断命令，用来连接到运行的 JVM 并输出详尽的类元数据的柱状图。
// *  jcmd PID VM.flags  查看虚拟机参数
// *  jcmd PID help 列出当前java进程可执行所有的操作
// *  jcmd PID help GC.class_stats  列出当前java进程执行此操作的参数选项
// *  jcmd PID PerfCounter.print 查看jvm性能相关的参数
// *  jcmd PID VM.uptime : 查看jvm的启动时长
// *  jcmd PID GC.class_histogram 查看进程的类信息
// *  jcmd PID Thread.print 查看线程的堆栈信息
// *  jcmd PID GC.heap_dump filename 到处heap dump文件，导出的文件可以通过jvisualvm查看
// *
// *
// *  jstack 可以查看或者导出java应用程序中的堆栈信息
// *
// *  jmc  有超级详细信息的控制台信息
// */
//public class MyTest4 {
//    public static void main(String[] args) {
//        for (;;){
//            Enhancer enhancer =  new Enhancer();
//            enhancer.setSuperclass(MyTest4.class);
//            enhancer.setUseCache(false);
//            enhancer.setCallback(new MethodInterceptor() {
//                @Override
//                public Object intercept(Object obj, Method method, Object[] ags, MethodProxy proxy) throws Throwable {
//                    return proxy.invokeSuper(obj, ags);
//                }
//            });
//            System.out.println("创建中");
//            enhancer.create();
////            try {
////                Thread.sleep(1);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }
//    }
//}
