package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "mypass";

    public Util() {
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
