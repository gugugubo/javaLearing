package com.gcb.jihe.concurrenthashmap;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @Author 古春波
 * @Description 使用不安全的集合类示例
 * @Date 2020/9/25 19:03
 * @Version 1.0
 **/

public class test1 {

    static final String ALPHA = "abcedfghijklmnopqrstuvwxyz";


/**
     * 你要做的是实现两个参数
     * 一是提供一个 map 集合，用来存放每个单词的计数结果，key 为单词，value 为计数
     * 二是提供一组操作，保证计数的安全性，会传递 map 集合以及 单词 List
     * 正确结果输出应该是每个单词出现 200 次
     * @param args*/
     

    public static void main(String[] args) {
        saveString();

        demo(
                (Supplier<Map<String, LongAdder>>) ConcurrentHashMap::new
                , 
                (map , words)->{
                    for (String word : words){
                        map.computeIfAbsent(word, (key)-> new LongAdder()).increment();
                    }
                }   
        );
        
        // 下面这种实现存在线程安全问题
//        demo(
//                ()->new HashMap<String, Integer>()
//                ,
//                (map , words)->{
//                    for (String word : words ){
//                        Integer count = map.get(word);
//                        count = count == null? 0 : count++;
//                        map.put(word,count);
//                    }
//                }
//        );
    }

/*
*
     * 你要做的是实现两个参数
     * 
     * 
     * 正确结果输出应该是每个单词出现 200 次
     * @param supplier 一是提供一个 map 集合，用来存放每个单词的计数结果，key 为单词，value 为计数
     * @param consumer 二是提供一组操作，保证计数的安全性，会传递 map 集合以及 单词 List
     * @param <V> 计数的类型
     */

    private static <V> void demo(Supplier<Map<String,V>> supplier,
                                 BiConsumer<Map<String,V>,List<String>> consumer) {
        Map<String, V> counterMap = supplier.get();
        List<Thread> ts = new ArrayList<>();
        for (int i = 1; i <= 26; i++) {
            int idx = i;
            Thread thread = new Thread(() -> {
                List<String> words = readFromFile(idx);
                consumer.accept(counterMap, words);
            });
            ts.add(thread);
        }
        ts.forEach(Thread::start);
        ts.forEach(t-> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(counterMap);
    }


/**
     * 保存信息到文件
     */

    public static void saveString() {
        int length = ALPHA.length();
        int count = 200;
        List<String> list = new ArrayList<>(length * count);
        for (int i = 0; i < length; i++) {
            char ch = ALPHA.charAt(i);
            // 向list中加入每个char两百次
            for (int j = 0; j < count; j++) {
                list.add(String.valueOf(ch));
            }
        }
        Collections.shuffle(list);
        for (int i = 0; i < 26; i++) {
            File outputFile = new File("src/main/resources/tmp/" + (i+1) + ".txt");
            try (FileWriter fr = new FileWriter(outputFile)) {

                String collect = String.join("\n", list.subList(i * count, (i + 1) * count));
                fr.write(collect);
                fr.close();
                System.out.println("已经将结果保存");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
/**
     * 从文件读取信息
     * @param i 文件名字
     * @return 文件信息
     */

    public static List<String> readFromFile(int i) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/tmp/"
                + i +".txt")))) {
            while(true) {
                String word = in.readLine();
                if(word == null) {
                    break;
                }
                words.add(word);
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
