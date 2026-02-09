package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    public static final String DB_URL = "jdbc:mysql://"
            + System.getenv("DB_HOST") + ":"
            + System.getenv("DB_PORT") + "/"
            + System.getenv("DB_NAME");
    public static final String DB_USER = System.getenv("DB_USER");
    public static final String DB_PASS = System.getenv("DB_PASS");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
