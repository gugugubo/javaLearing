package com.concurrent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
public class Main{


    static HashMap<Character, Integer> hashMap = new HashMap<>();
       
    public static void main(String[] args){
    }
    
    
    public  static int getRes(int i , String s){
        int index = 0;
        int res = 0;
       int temp = i;
        while (i<s.length() -1 && s.charAt(i+1) >'0' && s.charAt(i+1) <='9'){
            index = index*10 + s.charAt(i+1) - '0';
            i++;
        }
        Integer integer = hashMap.get(s.charAt(temp));
        if (integer==null){
            integer = 0;
        }
        if (index == 0){
            res += integer;
        }else {
            res += integer * index;
        }
        return res;
    }
}


