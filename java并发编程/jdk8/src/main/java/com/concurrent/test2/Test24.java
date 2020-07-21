package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
/**
 * 使用不同类型的线程池来解决线程饥饿问题
 */
public class Test24 {


    static final List<String> MENU = Arrays.asList("a", "b", "c", "d");
    static Random RANDOM = new Random();

    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        ExecutorService waiter = Executors.newFixedThreadPool(2);
        ExecutorService cooker = Executors.newFixedThreadPool(2);

        waiter.execute(() -> {
            log.info("点餐");
            Future<String> f1 = cooker.submit(() -> {
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

        waiter.execute(() -> {
            log.info("点餐");
            Future<String> f1 = cooker.submit(() -> {
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
    }

}
