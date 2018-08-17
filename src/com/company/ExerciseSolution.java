package com.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseSolution {
    private int id;
    private String created;
    private String updated;
    private String description;
    private int exerciseId;
    private int userId;

    public ExerciseSolution() {}

    public ExerciseSolution(String description, int exerciseId, int userId) {
//        ZoneId zoneId = ZoneId.of("Europe/Paris");
//        LocalDate today = LocalDate.now(zoneId);
//        this.created = today.toString();
//        this.updated = today.toString();
        this.description = description;
        this.exerciseId = exerciseId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

//    unikatowe pole
//    public void setId(int id) {
//        this.id = id;
//    }

    public void setId(int id) {this.id = id;}

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void saveToDB(Connection conn) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO Solutions(created, exercise_id, user_id) VALUES (NOW(), ?, ?)";
            String generatedColumns[] = { "ID" };
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql, generatedColumns);
//            preparedStatement.setString(1, this.created);
//            preparedStatement.setString(2, this.updated);
//            preparedStatement.setString(1, this.description);
            preparedStatement.setInt(1, this.exerciseId);
            preparedStatement.setInt(2, this.userId);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE Solutions SET description=?, updated=NOW() where id = ?";
//            ZoneId zoneId = ZoneId.of("Europe/Paris");
//            LocalDate today = LocalDate.now(zoneId);
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, this.description);
//            preparedStatement.setString(2, today.toString());
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }
    }

    static public ExerciseSolution loadExerciseSolutionById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Solutions where id=?";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            ExerciseSolution loadedExerciseSolution = new ExerciseSolution();
            loadedExerciseSolution.id = resultSet.getInt("id");
            loadedExerciseSolution.created = resultSet.getString("created");
            loadedExerciseSolution.updated = resultSet.getString("updated");
            loadedExerciseSolution.description = resultSet.getString("description");
            loadedExerciseSolution.exerciseId = resultSet.getInt("exercise_id");
            loadedExerciseSolution.userId = resultSet.getInt("user_id");
            return loadedExerciseSolution;
        }
        return null;
    }

    static public ArrayList<ExerciseSolution> loadAllExercisesSolutions(Connection conn) throws SQLException {
        ArrayList<ExerciseSolution> exercisesSolutions = new ArrayList<ExerciseSolution>();
        String sql = "SELECT * FROM Solutions";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        solutionsDetailling((ArrayList<ExerciseSolution>) exercisesSolutions, resultSet);
        return exercisesSolutions;
    }

    static public ArrayList<ExerciseSolution> loadAllByUserId(Connection conn, int userId) throws SQLException {
        ArrayList<ExerciseSolution> userSolutions = new ArrayList<ExerciseSolution>();
        String sql = "SELECT * FROM Solutions where user_id = ?";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        solutionsDetailling((ArrayList<ExerciseSolution>) userSolutions, resultSet);
        return userSolutions;
    }

    static public ArrayList<ExerciseSolution> loadAllByExerciseId(Connection conn, int exerciseId) throws SQLException {
        ArrayList<ExerciseSolution> exerciseSolutions = new ArrayList<ExerciseSolution>();
        String sql = "SELECT * FROM Solutions where exercise_id = ? order by updated ASC";
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, exerciseId);
        ResultSet resultSet = preparedStatement.executeQuery();
        solutionsDetailling((ArrayList<ExerciseSolution>) exerciseSolutions, resultSet);
        return exerciseSolutions;
    }

    private static void solutionsDetailling(ArrayList<ExerciseSolution> userSolutions, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            ExerciseSolution loadedExerciseSolution = new ExerciseSolution();
            loadedExerciseSolution.id = resultSet.getInt("id");
            loadedExerciseSolution.created = resultSet.getString("created");
            loadedExerciseSolution.updated = resultSet.getString("updated");
            loadedExerciseSolution.description = resultSet.getString("description");
            loadedExerciseSolution.exerciseId = resultSet.getInt("exercise_id");
            loadedExerciseSolution.userId = resultSet.getInt("user_id");
            userSolutions.add(loadedExerciseSolution);
        }
    }

    public void delete(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM Solutions WHERE id=?";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }
}
