package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS users (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            last_name VARCHAR(255),
            age INT
        );
    """;

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users;";
    private static final String CREATE_USER_SQL = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?);";
    private static final String SELECT_ALL_USERS_SQL = "SELECT id, name, last_name, age FROM users ORDER BY id;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String TRUNCATE_USERS_SQL = "TRUNCATE TABLE users;";

    private final Connection conn = new Util().getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Statement st = conn.createStatement()) {
            st.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement st = conn.createStatement()) {
            st.execute(DROP_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement ps = conn.prepareStatement(CREATE_USER_SQL)) {
            ps.setObject(1, name);
            ps.setObject(2, lastName);
            ps.setObject(3, age);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_USER_SQL)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS_SQL); ResultSet rs = ps.executeQuery()) {
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
        try (Statement st = conn.createStatement()) {
            st.execute(TRUNCATE_USERS_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
