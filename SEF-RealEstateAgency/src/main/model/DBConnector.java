package main.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {
    private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "1234";
    private final static String DATABASE = "realestate";

    public Connection getConnection() {
        try {
            Class.forName(DRIVER_NAME);
            Connection connection = DriverManager.getConnection(URL + DATABASE + "?autoReconnect=true&useSSL=false", USERNAME, PASSWORD);
//            System.out.println("[MYSQL] Connection successful!");
            return connection;
        } catch (Exception e) {
            System.out.println("[MYSQL] Unable to connect to database");
            e.printStackTrace();
            return null;
        }
    }
}
