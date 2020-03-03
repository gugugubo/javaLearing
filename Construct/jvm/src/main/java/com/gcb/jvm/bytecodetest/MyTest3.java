package com.gcb.jvm.bytecodetest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

/**
 * 可以分析字节码对异常的处理情况
 */
public class MyTest3 {

    public static void main(String[] args) {
        try  {
            InputStream in = new FileInputStream("test.txt");
            ServerSocket serverSocket = new ServerSocket(9999);

            serverSocket.accept();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("finally");
        }
    }
}
