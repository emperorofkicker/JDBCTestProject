package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS users (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            last_name VARCHAR(255),
            age INT
        );
    """;

    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users;";
    public static final String CREATE_USER_SQL = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?);";
    public static final String SELECT_ALL_USERS_SQL = "SELECT id, name, last_name, age FROM users ORDER BY id;";
    public static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?;";
    public static final String TRUNCATE_USERS_SQL = "TRUNCATE TABLE users;";

    private void executeNoRes(String query, Object... params) {
        try (Connection conn = Util.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        executeNoRes(CREATE_TABLE_SQL);
    }

    public void dropUsersTable() {
        executeNoRes(DROP_TABLE_SQL);
    }

    public void saveUser(String name, String lastName, byte age) {
        executeNoRes(CREATE_USER_SQL, name, lastName, age);
    }

    public void removeUserById(long id) {
        executeNoRes(DELETE_USER_SQL, id);
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try (Connection conn = Util.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS_SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                    rs.getString("name"),
                    rs.getString("last_name"),
                    rs.getByte("age")
                );

                user.setId(rs.getLong("id"));
                allUsers.add(user);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allUsers;
    }

    public void cleanUsersTable() {
        executeNoRes(TRUNCATE_USERS_SQL);
    }
}
