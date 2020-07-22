package com.concurrent.test2;

import com.concurrent.test.utils;

public class Test2 {

    static boolean run = true;
    
    final static Object lock =  new Object();
    
    public static void main(String[] args) {
//        Thread thread = new Thread(() -> {
//            while (true){
//                // 这样还是有可见性问题
//                if (!run){
//                    break;
//                }
//            }
//            
//        }, "线程");
//        thread.start();        
        
        Thread thread = new Thread(() -> {
            while (true){
                // 套上synchronized就不会有问题了
                synchronized (lock){
                    if (!run){
                        break;
                    }
                }
            }
            
        }, "线程");
        thread.start();

        utils.sleep(1);
        
        synchronized (lock){
            run = false;
        }
        
    }
    
}
