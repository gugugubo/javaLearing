package com.concurrent.test2;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Test39 {

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(2); // 个数为2时才会继续执行
        for (int i=0;i<3;i++){
            new Thread(()->{
                System.out.println("线程1开始.."+new Date());
                try {
                    cb.await(); // 当个数不足时，等待
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1继续向下运行..."+new Date());
            }).start();
            
            
            new Thread(()->{
                System.out.println("线程2开始.."+new Date());
                try { 
                    Thread.sleep(2000); 
                } catch (InterruptedException e) { 
                    
                }
                try {
                    cb.await(); // 2 秒后，线程个数够2，继续运行
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2继续向下运行..."+new Date());
            }).start();
        }
    }
    
}
