package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import static com.concurrent.test.test.getObjectHeader;

@Slf4j
public class Test18 {

    public static void main(String[] args) throws InterruptedException {
        Test1 t = new Test1();
        t.hashCode();
        test.parseObjectHeader(getObjectHeader(t));

        synchronized (t){
            test.parseObjectHeader(getObjectHeader(t));
        }
        test.parseObjectHeader(getObjectHeader(t));
    }
}
