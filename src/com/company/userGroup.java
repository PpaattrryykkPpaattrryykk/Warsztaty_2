package com.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class userGroup {
    private int id;
    private String className;

    public userGroup(String name) {
        this.className = name;
    }

    public userGroup() {}

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setId(int id) {this.id = id; }

    public int getId() {
        return id;
    }

    public void saveToDB(Connection conn) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO user_groups(name) VALUES (?)";
            String generatedColumns[] = { "ID" };
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.className);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE user_groups SET name=? where id = ?";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, this.className);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }
    }

    static public userGroup loadClassById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM user_groups where id=?";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            userGroup loadedClass = new userGroup();
            loadedClass.id = resultSet.getInt("id");
            loadedClass.className = resultSet.getString("name");
            return loadedClass;
        }
        return null;
    }

    static public ArrayList<userGroup> loadAllClasses(Connection conn) throws SQLException {
        ArrayList<userGroup> classes = new ArrayList<userGroup>();
        String sql = "SELECT * FROM user_groups";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            userGroup loadedClass = new userGroup();
            loadedClass.id = resultSet.getInt("id");
            loadedClass.className = resultSet.getString("name");
            classes.add(loadedClass);
        }
        return classes;
    }

    public void delete(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM user_groups WHERE id=?";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

}
