package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


@Slf4j
/**
 * 测试固定大小的线程池由于没有进行任务划分导致的饥饿问题
 */
public class Test23 {

    static final List<String> MENU = Arrays.asList("a","b", "c", "d");
    static Random RANDOM = new Random();
    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        pool.execute(()->{
            log.info("点餐");
            Future<String> f1 = pool.submit(() -> {
                log.info("做菜");
                return cooking();
            });

            try {
                log.debug("上菜: {}", f1.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });        
        
//        pool.execute(()->{
//            log.info("点餐");
//            Future<String> f1 = pool.submit(() -> {
//                log.info("做菜");
//                return cooking();
//            });
//
//            try {
//                log.debug("上菜: {}", f1.get());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        });

        
    }
    
}
