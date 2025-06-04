package org.example.murilo.ordemservico.repository;

import org.example.murilo.ordemservico.domain.entity.User;
import org.example.murilo.ordemservico.enumeration.UserRoleEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private final Connection conn;

    public UserRepository(final Connection conn) {
        this.conn = conn;
    }

    public void createUserTable() throws SQLException {
        PreparedStatement st = null;
        try {
            final String sql = "CREATE TABLE IF NOT EXISTS user (" +
                               "   id integer PRIMARY KEY AUTOINCREMENT," +
                               "   username text UNIQUE NOT NULL," +
                               "   name VARCHAR(100) NOT NULL," +
                               "   password VARCHAR(100) NOT NULL," +
                               "   role VARCHAR(20) NOT NULL" +
                               ");";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public void insertUserDefaultData() throws SQLException {
        PreparedStatement st = null;
        try {
            final String sql = """
                INSERT OR IGNORE INTO user (username, name, password, role)
                VALUES ('admin', 'Admin', 'admin', 'ADM'),
                       ('teste', 'Teste', 'teste', 'COMUM'),
                       ('danilo', 'Danilo', 'senha123', 'COMUM');
                """;
            st = conn.prepareStatement(sql);
            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public User findOneByUsername(final String username) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM user WHERE username = ? LIMIT 1");
            st.setString(1, username);

            rs = st.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("password"),
                    UserRoleEnum.getById(rs.getString("role").toLowerCase())
                );
            }

            return null;
        }  finally {
            Database.closeStatement(st);
            Database.closeResultSet(rs);
            Database.disconnect();
        }
    }

    public boolean existsByUsername(final String username) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT 1 FROM user WHERE username = ? LIMIT 1");
            st.setString(1, username);

            rs = st.executeQuery();

            return rs.next();
        }  finally {
            Database.closeStatement(st);
            Database.closeResultSet(rs);
            Database.disconnect();
        }
    }

    public void create(final User user) throws SQLException {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("INSERT INTO user (username, name, password, role) VALUES (?, ?, ?, ?)");

            st.setString(1, user.getUsername());
            st.setString(2, user.getName());
            st.setString(3, user.getPassword());
            st.setString(4, user.getRole().toString());

            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public void update(final User user) throws SQLException {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE user SET username = ?, name = ?, password = ? WHERE id = ?");

            st.setString(1, user.getUsername());
            st.setString(2, user.getName());
            st.setString(3, user.getPassword());
            st.setInt(4, user.getId());

            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }

    public void delete(final int id) throws SQLException {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM user WHERE id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } finally {
            Database.closeStatement(st);
            Database.disconnect();
        }
    }
}
