package com.company;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UserManagement {
    public static void main(String[] args) {
        DBConnection connection = new DBConnection(
                "jdbc:mysql://localhost:3306/DB_Warsztaty2?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"
        );

        try {
            boolean keepWorking = true;
            while (keepWorking) {
                ArrayList<User> allUsers = new ArrayList<User>();
                allUsers.addAll(User.loadAllUsers(connection.getConnection()));
                for (int i = 0; i < allUsers.size(); i++) {
                    System.out.println("Nazwa użytkownika: " + allUsers.get(i).getUserName() + ", email: " + allUsers.get(i).getEmail());
                }
                System.out.println("\nWybierz jedną z opcji:\n\nadd - dodanie użytkownika,\nedit - edycja użytkownika,\ndelete - usunięcie użytkownika,\nquit - zakończenie programu.");
                String userChoice = userChoice();
                if (userChoice.equals("add")) {
                    ArrayList<String> response = new ArrayList<String>();
                    response.addAll(userAdding());
                    User addedUser = new User(response.get(0), response.get(1), response.get(2));
                    try {
                        addedUser.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                    e.printStackTrace();
                    }
                } else if (userChoice.equals("edit")) {
                    ArrayList<String> response = new ArrayList<String>();
                    response.addAll(userEditting());
                    User userToEdit = new User(response.get(0), response.get(1), response.get(2));
                    userToEdit.setId(Integer.parseInt(response.get(3)));
                    try {
                        userToEdit.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (userChoice.equals("delete")) {
                    User userToDelete = new User();
                    userToDelete.setId(idToDelete());
                    try {
                        userToDelete.delete(connection.getConnection());
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
                System.out.println("\nZŁY WYBÓR \nWybierz jedną z opcji:\n\nadd - dodanie użytkownika,\nedit - edycja użytkownika,\ndelete - usunięcie użytkownika,\nquit - zakończenie programu.");
            }
        }
        return userResponse;
    }

    public static ArrayList<String> userAdding() {
        ArrayList<String> info = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwę użytkownika");
        String userName = scanner.next();
        info.add(userName);
        System.out.println("Podaj e-mail użytkownika");
        String email = scanner.next();
        info.add(email);
        System.out.println("Podaj hasło użytkownika");
        String password = scanner.next();
        info.add(password);
        return info;
    }

    public static ArrayList<String> userEditting() {
        ArrayList<String> editedInfo = new ArrayList<String>();
        editedInfo.addAll(userAdding());
        System.out.println("Podaj id użytkownika do edycji:");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForInt(scanner, waitForInt, userInput);
        String id = Integer.toString(userInput);
        editedInfo.add(id);
        return editedInfo;
    }

    public static int idToDelete() {
        System.out.println("Podaj id użytkownika do usunięcia:");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForInt(scanner, waitForInt, userInput);
        return userInput;
    }

    private static int waitForInt(Scanner scanner, boolean waitForInt, int userInput) {
        userInput = UserSolutionsManagement.waitForUsersId(scanner, waitForInt, userInput);
        return userInput;
    }
}
