package com.company;


import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private int id;
    private String userName;
    private String password;
    private String email;

    public User(String username, String email, String password) {
        this.userName = username;
        this.email = email;
        this.setPassword(password);
    }

    public User() { }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void saveToDB(Connection conn) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO Users(name, email, password) VALUES (?, ?, ?)";
            String generatedColumns[] = { "ID" };
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE Users SET name=?, email=?, password=? where id = ?";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setInt(4, this.id);
            preparedStatement.executeUpdate();
        }
    }

    static public User loadUserById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Users where id=?";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return userDetails(resultSet);
        }
        return null;
    }

    static public User loadUserByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT * FROM Users where email=?";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return userDetails(resultSet);
        }
        return null;
    }

    public static User userDetails(ResultSet resultSet) throws SQLException {
        User loadedUser = new User();
        loadedUser.id = resultSet.getInt("id");
        loadedUser.userName = resultSet.getString("name");
        loadedUser.password = resultSet.getString("password");
        loadedUser.email = resultSet.getString("email");
        return loadedUser;
    }

    public static ArrayList<User> loadAllUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM Users";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        userDetailling((ArrayList<User>) users, resultSet);
        return users;
    }

    public static ArrayList<User> loadAllByGroupId(Connection conn, int id) throws SQLException {
        ArrayList<User> usersFromGroup = new ArrayList<User>();
        String sql = "SELECT * FROM Users where user_group_id = ?";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        userDetailling((ArrayList<User>) usersFromGroup, resultSet);
        return usersFromGroup;
    }

    private static void userDetailling(ArrayList<User> usersFromGroup, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            User loadedUser = new User();
            loadedUser.id = resultSet.getInt("id");
            loadedUser.userName = resultSet.getString("name");
            loadedUser.password = resultSet.getString("password");
            loadedUser.email = resultSet.getString("email");
            usersFromGroup.add(loadedUser);
        }
    }

    public void delete(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM Users WHERE id=?";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

//    try {
//        connection.closeConnection();
//        } catch (SQLException e) {
//        e.printStackTrace();
//        }

}