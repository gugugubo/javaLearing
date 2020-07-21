package com.concurrent.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Test34 {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<?> submit = pool.submit(() -> {
            System.out.println("2323");
        });
        System.out.println("sdfsdf"  + submit);
    }
    
}

            