package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j(topic = "c.Test4")
public class Test4 {

    static String fileName = "D:\\JavaLearing\\java并发编程\\jdk8\\src\\main\\resources\\test";

    public static void main(String[] args) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                log.debug("我是一个新建的线程正在运行中");
                FileReader.read(fileName);
            }
        };
        thread.setName("新建线程");
        thread.run();

        Thread thread1 = new Thread();
        thread1.run();
        log.debug("主线程");
    }

    public static void fileReader() {
        FileReader.read(fileName);
    }

}

@Slf4j(topic = "c.FileReader")
class FileReader {
    public static void read(String filename) {
        int idx = filename.lastIndexOf(File.separator);
        String shortName = filename.substring(idx + 1);
        try (FileInputStream in = new FileInputStream(filename)) {
            long start = System.currentTimeMillis();
            log.debug("read [{}] start ...", shortName);
            byte[] buf = new byte[1024];
            int n = -1;
            do {
                n = in.read(buf);
            } while (n != -1);
            long end = System.currentTimeMillis();
            log.debug("read [{}] end ... cost: {} ms", shortName, end - start);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}