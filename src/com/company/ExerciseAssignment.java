package com.company;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.UserSolutionsManagement.waitForUsersId;

public class ExerciseAssignment {
    public static void main(String[] args) {
        DBConnection connection = new DBConnection(
                "jdbc:mysql://localhost:3306/DB_Warsztaty2?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"
        );

        try {
            boolean keepWorking = true;
            while (keepWorking) {
                System.out.println("\nWybierz jedną z opcji:\n\nadd - przypisywanie zadań do użytkowników,\nview - przeglądanie rozwiązań danego użytkownika,\nquit - zakończenie programu.");
                String userChoice = userChoice();
                if (userChoice.equals("add")) {
                    ArrayList<User> allUsers = new ArrayList<User>();
                    allUsers.addAll(User.loadAllUsers(connection.getConnection()));
                    for (int i = 0; i < allUsers.size(); i++) {
                        System.out.println("ID użytkownika: " + allUsers.get(i).getId() + " Nazwa użytkownika: " + allUsers.get(i).getUserName() + ", email: " + allUsers.get(i).getEmail());
                    }
                    System.out.println("Podaj ID użytkownika do przypisania zadania");
                    int userId = getId();
                    ArrayList<Exercise> allExercises = new ArrayList<Exercise>();
                    allExercises.addAll(Exercise.loadAllExercises(connection.getConnection()));
                    for (int i = 0; i < allExercises.size(); i++) {
                        System.out.println("Tytuł zadania: " + allExercises.get(i).getTitle() + ", opis zadania: " + allExercises.get(i).getDescription() + ", id zadania: " + allExercises.get(i).getId());
                    }
                    System.out.println("Podaj ID zadania do przypisania");
                    int exerciseId = getId();
                    try {
                        ExerciseSolution solution = new ExerciseSolution();
                        solution.setUserId(userId);
                        solution.setExerciseId(exerciseId);
                        solution.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (userChoice.equals("view")) {
                    System.out.println("Podaj ID użytkownika do wyświetlenia rozwiązań");
                    int userId = getId();
                    ArrayList<ExerciseSolution> allSolutions = new ArrayList<>();
                    allSolutions.addAll(ExerciseSolution.loadAllByUserId(connection.getConnection(), userId));
                    for (int i = 0; i < allSolutions.size(); i++) {
                        System.out.println("Utworzono: " + allSolutions.get(i).getCreated() + ", dodano rozwiązanie: " + allSolutions.get(i).getUpdated() + ", opis: " + allSolutions.get(i).getDescription() + ", id zadania: " + allSolutions.get(i).getExerciseId());
                    }
                } else {
                    keepWorking = false;
                }
            }
        } catch (SQLException e){
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
            if (userResponse.equals("add") || userResponse.equals("view") || userResponse.equals("quit")) {
                keepWorking = false;
            } else {
                System.out.println("\nZŁY WYBÓR \nWybierz jedną z opcji:\n\nadd - przypisywanie zadań do użytkowników,\nview - przeglądanie rozwiązań danego użytkownika,\nquit - zakończenie programu.");
            }
        }
        return userResponse;
    }

    public static int getId() {
//        System.out.println("Podaj id użytkownika do przypisania zadania");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForUsersId(scanner, waitForInt, userInput);
        return userInput;
    }

}
