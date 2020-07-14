package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

/**
 * 可见性问题
 */
@Slf4j
public class Test1 {
    static boolean run = true;   // 在这里加上volatile就行了
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(run){
                // ....
//                System.out.println(2323);  
//                如果加上这个代码就会停下来，因为println方法里面有synchronized修饰
            }
        });
        t.start();
        utils.sleep(1);
        System.out.println(3434);   
        run = false; // 线程t不会如预想的停下来
    }
}
