package com.company;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GroupManagement {
    public static void main(String[] args) {
        DBConnection connection = new DBConnection(
                "jdbc:mysql://localhost:3306/DB_Warsztaty2?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"
        );

        try {
            boolean keepWorking = true;
            while (keepWorking) {
                ArrayList<userGroup> allClasses = new ArrayList<userGroup>();
                allClasses.addAll(userGroup.loadAllClasses(connection.getConnection()));
                for (int i = 0; i < allClasses.size(); i++) {
                    System.out.println("Nazwa klasy: " + allClasses.get(i).getClassName() + ", id klasy: " + allClasses.get(i).getId());
                }
                System.out.println("\nWybierz jedną z opcji:\n\nadd - dodanie klasy,\nedit - edycja klasy,\ndelete - usunięcie klasy,\nquit - zakończenie programu.");
                String userChoice = userChoice();
                if (userChoice.equals("add")) {
                    ArrayList<String> requiredData = new ArrayList<String>();
                    requiredData.addAll(classAdding());
                    userGroup addedClass = new userGroup(requiredData.get(0));
                    try {
                        addedClass.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (userChoice.equals("edit")) {
                    ArrayList<String> requiredData = new ArrayList<String>();
                    requiredData.addAll(classEditting());
                    userGroup classToEdit = new userGroup (requiredData.get(0));
                    classToEdit.setId(Integer.parseInt(requiredData.get(1)));
                    try {
                        classToEdit.saveToDB(connection.getConnection());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (userChoice.equals("delete")) {
                    userGroup classToDelete = new userGroup();
                    classToDelete.setId(idToDelete());
                    try {
                        classToDelete.delete(connection.getConnection());
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
                System.out.println("\nZŁY WYBÓR \nWybierz jedną z opcji:\n\nadd - dodanie klasy,\nedit - edycja klasy,\ndelete - usunięcie klasy,\nquit - zakończenie programu.");
            }
        }
        return userResponse;
    }

    public static ArrayList<String> classAdding() {
        ArrayList<String> info = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwę klasy");
        String className = scanner.nextLine();
        info.add(className);
        return info;
    }

    public static ArrayList<String> classEditting() {
        ArrayList<String> editedInfo = new ArrayList<String>();
        editedInfo.addAll(classAdding());
        System.out.println("Podaj id klasy do edycji:");
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
        System.out.println("Podaj id klasy do usunięcia:");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForInt(scanner, waitForInt, userInput);
        return userInput;
    }

}
