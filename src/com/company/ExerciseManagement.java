package com.company;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExerciseManagement {
    public static void main(String[] args) {
        DBConnection connection = new DBConnection(
                "jdbc:mysql://localhost:3306/DB_Warsztaty2?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"
        );

        try {
            boolean keepWorking = true;
            while (keepWorking) {
                ArrayList<Exercise> allExercises = new ArrayList<Exercise>();
                allExercises.addAll(Exercise.loadAllExercises(connection.getConnection()));
                for (int i = 0; i < allExercises.size(); i++) {
                    System.out.println("Tytuł zadania: " + allExercises.get(i).getTitle() + ", opis zadania: " + allExercises.get(i).getDescription() + ", id zadania: " + allExercises.get(i).getId());
                }
                System.out.println("\nWybierz jedną z opcji:\n\nadd - dodanie zadania,\nedit - edycja zadania,\ndelete - usunięcie zadania,\nquit - zakończenie programu.");
                String userChoice = userChoice();
                if (userChoice.equals("add")) {
                    ArrayList<String> requiredData = new ArrayList<String>();
                    requiredData.addAll(exerciseAdding());
                    Exercise addedExercise = new Exercise(requiredData.get(0), requiredData.get(1));
                    try {
                        addedExercise.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (userChoice.equals("edit")) {
                    ArrayList<String> requiredData = new ArrayList<String>();
                    requiredData.addAll(exerciseEditting());
                    Exercise exerciseToEdit = new Exercise(requiredData.get(0), requiredData.get(1));
                    exerciseToEdit.setId(Integer.parseInt(requiredData.get(2)));
                    try {
                        exerciseToEdit.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (userChoice.equals("delete")) {
                    Exercise exerciseToDelete = new Exercise();
                    exerciseToDelete.setId(idToDelete());
                    try {
                        exerciseToDelete.delete(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    keepWorking = false;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }



        try {
            connection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String userChoice() {
        Scanner scanner = new Scanner(System.in);
        boolean keepWorking = true;
        String userResponse = "";
        while (keepWorking) {
            userResponse = scanner.next();
            if (userResponse.equals("add") || userResponse.equals("edit") || userResponse.equals("delete") || userResponse.equals("quit")) {
                keepWorking = false;
            } else {
                System.out.println("\nZŁY WYBÓR \nWybierz jedną z opcji:\n\nadd - dodanie zadania,\nedit - edycja zadania,\ndelete - usunięcie zadania,\nquit - zakończenie programu.");
            }
        }
        return userResponse;
    }

    public static ArrayList<String> exerciseAdding() {
        ArrayList<String> info = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj tytuł zadania");
        String exerciseName = scanner.nextLine();
        info.add(exerciseName);
        System.out.println("Podaj opis zadania");
        String exerciseDescription = scanner.nextLine();
        info.add(exerciseDescription);
        return info;
    }

    public static ArrayList<String> exerciseEditting() {
        ArrayList<String> editedInfo = new ArrayList<String>();
        editedInfo.addAll(exerciseAdding());
        System.out.println("Podaj id zadania do edycji:");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForInt(scanner, waitForInt, userInput);
        String id = Integer.toString(userInput);
        editedInfo.add(id);
        return editedInfo;
    }

    private static int waitForInt(Scanner scanner, boolean waitForInt, int userInput) {
        userInput = UserSolutionsManagement.waitForUsersId(scanner, waitForInt, userInput);
        return userInput;
    }

    public static int idToDelete() {
        System.out.println("Podaj id zadania do usunięcia:");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForInt(scanner, waitForInt, userInput);
        return userInput;
    }

}
