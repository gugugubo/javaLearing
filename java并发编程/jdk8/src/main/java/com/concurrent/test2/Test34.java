package com.concurrent.test2;

import java.util.concurrent.*;

public class Test34 {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<?> submit = pool.submit(() -> {
            System.out.println("2323");
            return "ok";
        });
        try {
            System.out.println("sdfsdf"  + submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
}

            