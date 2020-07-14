package com.concurrent.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class utils {
    public static List<String> download(){
        List<String> response = new ArrayList<>();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response.add(getRandomString(4));
        response.add(getRandomString(4));
        return response;
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void sleep(int i) {
        try {
            Thread.sleep(i*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
