package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import static com.concurrent.test.test.getObjectHeader;


@Slf4j
public class Test19 {

    public static void main(String[] args) {
        Test1 t = new Test1();
        new Thread(()->{
            test.parseObjectHeader(getObjectHeader(t));
            synchronized (t){
                test.parseObjectHeader(getObjectHeader(t));
            }
            test.parseObjectHeader(getObjectHeader(t));


            synchronized (Test19.class){
                    Test19.class.notify();
            }

        },"我是线程一").start();

        new Thread(()->{
            synchronized (Test19.class){
                try {
                    Test19.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            test.parseObjectHeader(getObjectHeader(t));

            synchronized (t){
                test.parseObjectHeader(getObjectHeader(t));
            }
            test.parseObjectHeader(getObjectHeader(t));
        },"我是线程二").start();
    }
}
