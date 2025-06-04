package org.example.murilo.ordemservico.repository;

import java.sql.*;

public class Database {

    private static Connection conn = null;

    public static Connection connect() throws SQLException {
        if (conn == null) {
            final String url = "jdbc:sqlite:db/sd.db";
            conn = DriverManager.getConnection(url);
        }

        return conn;
    }

    public static void disconnect() throws SQLException {

        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    public static void closeStatement(final Statement st) throws SQLException {

        if (st != null) {
            st.close();
        }
    }

    public static void closeResultSet(final ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }
}
