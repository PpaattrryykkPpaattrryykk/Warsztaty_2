package com.company;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection;

    public DBConnection(String url, String user, String password) {
        try {
            this.connection = (Connection) DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
