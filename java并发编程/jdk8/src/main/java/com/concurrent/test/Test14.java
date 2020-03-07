package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test14 {
    public static void main(String[] args) throws InterruptedException {
        synchronizedObject synchronizedObject = new synchronizedObject();
        Thread t1 = new Thread(()->{
            for (int i = 1;i<5000;i++){
               synchronizedObject.up();
            }
        });
        Thread t2 =new Thread(()->{
            for (int i = 1;i<5000;i++){
               synchronizedObject.down();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("count的值是{}",synchronizedObject.getCount());
    }
}

class synchronizedObject {
    int count = 0;
    Object object = new Object();

    public void up(){
        synchronized (object){
            count = count +1;
        }
    }

    public void down(){
        synchronized (object){
            count = count - 1;
        }
    }

    public int getCount(){
        return count;
    }


}
