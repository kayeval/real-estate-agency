package main.model.DBModel;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {
    private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/realestate";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "1234";
    //    private final static String DATABASE = "realestate";
    private ComboPooledDataSource comboPooledDataSource;

    public DBConnector() {
        initializeConnection();
    }

    public void initializeConnection() {
        comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
            comboPooledDataSource.setJdbcUrl(URL);
            comboPooledDataSource.setUser(USERNAME);
            comboPooledDataSource.setPassword(PASSWORD);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //    public Connection getConnection() {
//        try {
//            Class.forName(DRIVER_NAME);
//            Connection connection = DriverManager.getConnection(URL + DATABASE + "?autoReconnect=true&useSSL=false", USERNAME, PASSWORD);
//            System.out.println("[MYSQL] Connection successful!");
//            return connection;
//        } catch (Exception e) {
//            System.out.println("[MYSQL] Unable to connect to database");
//            e.printStackTrace();
//            return null;
//        }
//    }
}
