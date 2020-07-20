package com.concurrent.test2;
import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j
/**
 * AtomicStampedReference 的使用，解决aba问题
 */
public class Test9 {
    
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);
    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        int stamp = ref.getStamp();
        log.info("{}",stamp);
        String prev = ref.getReference();
        other();
        utils.sleep(1);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C",stamp,stamp+1));
    }
    
    private static void other() {
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.info("{}",stamp);
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B",stamp,stamp+1));
        }, "t1").start();
        utils.sleep(1);
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.info("{}",stamp);
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A",stamp,stamp+1));
        }, "t2").start();
    }
}
