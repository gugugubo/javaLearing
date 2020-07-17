package com.concurrent.test2;


import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


@Slf4j
public class Test21 {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
//        method1(pool);

//        method2(pool);
        method3(pool);
    }


    /**
     * 带有返回结果的执行任务方法invokeAll测试
     */
    private static void method2 (ExecutorService pool) {
        try {
            List<Future<Object>> futures = pool.invokeAll(
                    Arrays.asList(
                            () -> {
                                log.info("11111");
                                return "1111";
                            },
                            () -> {
                                log.info("2222");
                                return "22222";
                            },
                            () -> {
                                log.info("3333");
                                return "33333";
                            }
                    )
            );
            futures.forEach( f ->{
                try {
                    log.info("{}",f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }


    /**
     * 带有返回结果的执行任务方法invokeAll测试
     */
    private static void method3 (ExecutorService pool) {
        try {
            Object o = null;
            try {
                o = pool.invokeAny(
                        Arrays.asList(
                                () -> {
                                    log.info("11111");
                                    return "1111";
                                },
                                () -> {
                                    log.info("2222");
                                    return "22222";
                                },
                                () -> {
                                    log.info("3333");
                                    return "33333";
                                }
                        )
                );
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            log.info("{}",o);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 带有返回结果的执行任务方法submit测试
     */
    private static void method1(ExecutorService pool) {
        Future<String> future = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("23232");
                utils.sleep(1);
                return "ok";
            }
        });

        try {
            log.info("{}",future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
