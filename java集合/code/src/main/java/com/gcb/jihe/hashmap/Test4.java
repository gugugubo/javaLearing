package com.gcb.jihe.hashmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author 古春波
 * @Description Iterator  删除元素测试
 * @Date 2020/11/18 15:50
 * @Version 1.0
 **/
public class Test4 {

    public static void main(String[] args) {
        
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        Iterator<String> it = a.iterator();
        while (it.hasNext()) {
            String temp = it.next();
            System.out.println("temp: " + temp);
            if("1".equals(temp)){
                a.remove(temp);
            }
        }
    }
}
