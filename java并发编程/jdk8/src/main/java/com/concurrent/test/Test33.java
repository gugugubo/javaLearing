package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
/**
 * 使用reentrantlock中的tryLock来获取锁来解决哲学家就餐问题，这样就不会造成死锁！
 */
public class Test33 extends Thread{

    public static void main(String[] args) {
        Chopstick2 c1 = new Chopstick2("1");
        Chopstick2 c2 = new Chopstick2("2");
        Chopstick2 c3 = new Chopstick2("3");
        Chopstick2 c4 = new Chopstick2("4");
        Chopstick2 c5 = new Chopstick2("5");
        new Philosopher2("苏格拉底", c1, c2).start();
        new Philosopher2("柏拉图", c2, c3).start();
        new Philosopher2("亚里士多德", c3, c4).start();
        new Philosopher2("赫拉克利特", c4, c5).start();
        new Philosopher2("阿基米德", c5, c1).start();
    }
    
}

@Slf4j(topic = "Philosopher")
class Philosopher2 extends Thread{
    Chopstick2 left;
    Chopstick2 right;
    public Philosopher2(String name, Chopstick2 left, Chopstick2 right) {
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
            try {
                if (left.tryLock(2, TimeUnit.SECONDS)){
                    try {
                        if (right.tryLock(2, TimeUnit.SECONDS)){
                            try {
                                eat();    
                            }finally {
                                right.unlock();
                            }
                        }
                    }finally {
                        left.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}

class Chopstick2 extends ReentrantLock {
    private String name ;

    public Chopstick2(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}
