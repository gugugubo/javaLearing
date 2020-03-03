package com.gcb.jvm.classload;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;


/**
 * 线程上下文类加载器的一般使用模式：（获取-使用-还原）
 * 伪代码：
 * ClassLoader classLoader=Thread.currentThread().getContextLoader();
 * try{
 * Thread.currentThread().setContextLoader(targetTccl);
 * myMethod();
 * }finally{
 * Thread.currentThread().setContextLoader(classLoader);
 * }
 * 在myMethod中调用Thread.currentThread().getContextLoader()做某些事情
 * ContextClassLoader的目的就是为了破坏类加载委托机制
 * <p>
 * 在SPI接口的代码中，使用线程上下文类加载器就可以成功的加载到SPI的实现类。
 * <p>
 * 当高层提供了统一的接口让底层去实现，同时又要在高层加载（或实例化）底层的类时，
 * 就必须通过上下文类加载器来帮助高层的ClassLoader找到并加载该类。
 */
public class classloadTest26 {

    public static void main(String[] args) {

        //Thread.currentThread().setContextClassLoader(classloadTest26.class.getClassLoader().getParent());
        /**
         * 如果使用上述代码，就会显式地修改上下文类加载器，这样就会导致无法加载Driver 的实现类
         */


        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);

        Iterator<Driver> iterator = load.iterator();

        while (iterator.hasNext()){

            Driver driver = iterator.next();
            System.out.println("drive" + driver.getClass() + ", loader" + driver.getClass().getClassLoader());
            /**
             * 输出
             * driveclass com.mysql.jdbc.Driver, loadersun.misc.Launcher$AppClassLoader@18b4aac2
             * driveclass com.mysql.fabric.jdbc.FabricMySQLDriver, loadersun.misc.Launcher$AppClassLoader@18b4aac2
             * 因为这两个实现类都在classpath路径下，所以都是系统类加载器加载的
             * 而这两个驱动都位于classpath路径下，在我没指定任何东西的情况下
             * ServiceLoader能加载出classpath下的驱动类的实现类到底是什么原因呢
             * ServiceLoader类中先通过Thread.getContextClassLoader获取ClassLoader再通过
             * 反射Class.forName 的方式将获取的ClassLoader指定为加载类的类加载器, 这样就成功用指定的类加载器加载了类！
             *
             * ServiceLoader.load -> ServiceLoader.load -> new ServiceLoader<>  ->reload()
             * ->new LazyIterator  ->nextService() -> Class.forName(cn, false, loader)
             */
        }

        System.out.println(Thread.currentThread().getContextClassLoader());
        //输出 ：sun.misc.Launcher$AppClassLoader@18b4aac2   没有任何设置的情况下，默认为系统类加载器
        System.out.println(ServiceLoader.class.getClassLoader());
        // 输出 ：null   ServiceLoader是Util包下的，由根类加载器加载
    }
}
