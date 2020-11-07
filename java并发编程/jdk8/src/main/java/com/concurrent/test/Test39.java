package com.concurrent.test;

import java.util.concurrent.locks.LockSupport;

public class Test39 {

    static Thread thread2 ;
    static Thread thread1;
    static Thread thread3;
    
    public static void main(String[] args) {

        ParkUnpark parkUnpark = new ParkUnpark(15);


        thread1 = new Thread(() -> {
            parkUnpark.print("a",thread2);
        }, "线程一");
        thread2 = new Thread(() -> {
            parkUnpark.print("b",thread3);
        }, "线程二");
        thread3 = new Thread(() -> {
            parkUnpark.print("c",thread1);
        }, "线程三");
        
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        LockSupport.unpark(thread1);
    }
    
    
}


class ParkUnpark{
    int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }
    
    public void print(String str , Thread next ){
        for (int i=0;i<loopNumber;i++){
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
    
    
}