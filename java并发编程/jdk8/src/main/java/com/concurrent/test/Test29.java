package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
/**
 * 此程序会发生死锁，使用jconsole查看，每个哲学家线程持有一个根筷子，并且等待另一个线程释放
 * -------------------------------------------------------------------------
 * 名称: 阿基米德
 * 状态: cn.itcast.Chopstick@1540e19d (筷子1) 上的BLOCKED, 拥有者: 苏格拉底
 * 总阻止数: 2, 总等待数: 1
 * 堆栈跟踪:
 * cn.itcast.Philosopher.run(TestDinner.java:48)
 *  - 已锁定 cn.itcast.Chopstick@6d6f6e28 (筷子5)
 * -------------------------------------------------------------------------
 * 名称: 苏格拉底
 * 状态: cn.itcast.Chopstick@677327b6 (筷子2) 上的BLOCKED, 拥有者: 柏拉图
 * 总阻止数: 2, 总等待数: 1
 * 堆栈跟踪:
 * cn.itcast.Philosopher.run(TestDinner.java:48)
 *  - 已锁定 cn.itcast.Chopstick@1540e19d (筷子1)
 * -------------------------------------------------------------------------
 * 名称: 柏拉图
 * 状态: cn.itcast.Chopstick@14ae5a5 (筷子3) 上的BLOCKED, 拥有者: 亚里士多德
 * 总阻止数: 2, 总等待数: 0
 * 堆栈跟踪:
 * cn.itcast.Philosopher.run(TestDinner.java:48)
 *  - 已锁定 cn.itcast.Chopstick@677327b6 (筷子2)
 * -------------------------------------------------------------------------
 * 名称: 亚里士多德
 * 状态: cn.itcast.Chopstick@7f31245a (筷子4) 上的BLOCKED, 拥有者: 赫拉克利特
 * 总阻止数: 1, 总等待数: 1
 * 堆栈跟踪:
 * cn.itcast.Philosopher.run(TestDinner.java:48)
 *  - 已锁定 cn.itcast.Chopstick@14ae5a5 (筷子3)
 * -------------------------------------------------------------------------
 * 名称: 赫拉克利特
 * 状态: cn.itcast.Chopstick@6d6f6e28 (筷子5) 上的BLOCKED, 拥有者: 阿基米德
 * 总阻止数: 2, 总等待数: 0
 * 堆栈跟踪:
 * cn.itcast.Philosopher.run(TestDinner.java:48)
 *  - 已锁定 cn.itcast.Chopstick@7f31245a (筷子4)
 */
public class Test29 extends Thread{

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
        /**
         * 如果这里把上面的任何一个加锁的顺序改变一下，那么就不会产生死锁了
         * 比如将new Philosopher("赫拉克利特", c4, c5).start();改成
         * new Philosopher("赫拉克利特", c5, c4).start();
         * 这样就不会死锁了，但是会出现另外一个问题，就是大部分的时间的是在执行
         * new Philosopher("亚里士多德", c3, c4).start();这个线程，其他线程很少执行的机会，称为线程饥饿
         * 
         *         Chopstick c1 = new Chopstick("1");
         *         Chopstick c2 = new Chopstick("2");
         *         Chopstick c3 = new Chopstick("3");
         *         Chopstick c4 = new Chopstick("4");
         *         Chopstick c5 = new Chopstick("5");
         *         new Philosopher("苏格拉底", c1, c2).start();
         *         new Philosopher("柏拉图", c2, c3).start();
         *         new Philosopher("亚里士多德", c3, c4).start();
         *         new Philosopher("赫拉克利特", c5, c4).start();
         *         new Philosopher("阿基米德", c5, c1).start();
         * 
         * 
         */
    }
    
    
   
}

@Slf4j(topic = "Philosopher")
class Philosopher extends Thread{
    Chopstick left;
    Chopstick right;
    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }
    private void eat() {
        log.debug("eating...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            // 获得左手筷子
            synchronized (left) {
                // 获得右手筷子
                synchronized (right) {
                    // 吃饭
                    eat();
                }
                // 放下右手筷子
            }
            // 放下左手筷子
        }
    }
}

class Chopstick{
    private String name ;

    public Chopstick(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}
