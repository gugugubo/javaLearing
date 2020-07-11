package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

import static com.concurrent.test.test.getObjectHeader;

@Slf4j
public class Test21 {

    public static void main(String[] args) throws InterruptedException {
        Test21.test3();
    }

    private static void test3() throws InterruptedException {
        Vector<Dog> list = new Vector<>();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Dog d = new Dog();
                list.add(d);
                synchronized (d) {
                    System.out.print(i);
                    test.parseObjectHeader(getObjectHeader(d));
                }
            }
            synchronized (list) {
                list.notify();
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("===============> ");
            for (int i = 0; i < 30; i++) {
                Dog d = list.get(i);
                System.out.print(i);
                test.parseObjectHeader(getObjectHeader(d));
                synchronized (d) {
                    System.out.print(i);
                    test.parseObjectHeader(getObjectHeader(d));
                }
//                Thread.sleep(1000);
                System.out.print(i);
//                Thread.interrupted();
                test.parseObjectHeader(getObjectHeader(d));
            }
        }, "t2");
        t2.start();
    }
}

class Dog{}
