package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {

    // 实现多线程的第一种方法
    public static void test2() {
        Thread t = new Thread(()->{ log.debug("running"); }, "t2");
        System.out.println(232323);
        t.start();
    }

    public static void main(String[] args) {
        test2();
        log.debug("ceshizhong");
    }
}
