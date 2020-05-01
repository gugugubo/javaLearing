package com.gcb.concurrency1;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用condition/lock锁
 */
public class MyTest8 {
    public static void main(String[] args) {
        BoundedContainer boundedContainer = new BoundedContainer();

        for (int i=0;i<20;i++){
            new Thread(()->{
                try {
                    boundedContainer.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i=0;i<20;i++){
            new Thread(()->{
                try {
                    boundedContainer.put("hello");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}

class BoundedContainer{
    private String[] elements = new String[10];
    private Lock lock = new ReentrantLock();

    private Condition notEmptyCondition = lock.newCondition();
    private Condition notFullCondition = lock.newCondition();

    private int elementCount;  // 已有元素的个数
    private int putIndex;
    private int takeIndex;

    public void put(String element) throws InterruptedException {
        this.lock.lock();
        try {
            while (this.elementCount == this.elements.length){
                this.notFullCondition.await();
            }
            elements[putIndex] = element;
            if ( ++putIndex == this.elements.length){
                putIndex = 0;
            }
            elementCount++;  // 元素加一
            System.out.println("put method " + Arrays.toString(elements));
            this.notEmptyCondition.signal();
        }finally {
            this.lock.unlock();
        }
    }
    public String take() throws InterruptedException {
        this.lock.lock();
        try {
            while (this.elementCount == 0){
                this.notEmptyCondition.await();
            }
            String element = elements[takeIndex];
            elements[takeIndex] = null;
            if (++takeIndex == this.elements.length){
                takeIndex = 0;
            }
            elementCount--;  // 元素加一
            System.out.println("take method " + Arrays.toString(elements));
            this.notFullCondition.signal();
            return element;
        }finally {
            this.lock.unlock();
        }
    }
}
