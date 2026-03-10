package utils;

import java.sql.*;

public class DBUtil {
    // Các thành phần
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/phonestore_db?currentSchema=storedb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1907";

    // Đóng và mở kết nối
    public static Connection getConnection() {
        // khai báo Driver
        try {
            Class.forName(DRIVER);
            // Mở kết nối
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
