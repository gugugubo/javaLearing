package com.gcb.jvm.classload;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class classloadTest27 {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
    }

}
