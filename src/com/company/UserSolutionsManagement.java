package com.company;

import sun.util.calendar.CalendarDate;
import sun.util.calendar.LocalGregorianCalendar;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Scanner;

public class UserSolutionsManagement {
    public static void main(String[] args) {

        DBConnection connection = new DBConnection(
                "jdbc:mysql://localhost:3306/DB_Warsztaty2?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"
        );

        int userId = userId();
        boolean keepWorking = true;
        while (keepWorking) {
            System.out.println("Wybierz jedną z opcji:\n\n1. add - dodanie rozwiązania,\n2. view - podgląd rozwiązań,\n3. quit - zakończenie programu.");
            String userSolutionsManagementChoice = userChoiceSolutionsManagement();
            if (userSolutionsManagementChoice.equals("add")) {
                try {
                    ArrayList<ExerciseSolution> allUserSolutions = new ArrayList<>();
                    allUserSolutions.addAll(ExerciseSolution.loadAllByUserId(connection.getConnection(), userId));
                    for (int i = 0; i < allUserSolutions.size(); i++) {
                        if (allUserSolutions.get(i).getUpdated() == null) {
                            System.out.println("ID rozwiązania: " + allUserSolutions.get(i).getId() + ", utworzono: " + allUserSolutions.get(i).getCreated() + ", id zadania: " + allUserSolutions.get(i).getExerciseId());
                        }
                    }
                    ArrayList<String> solutionDetails;
                    solutionDetails = solutionAdding();
                    ExerciseSolution solutionToAdd = new ExerciseSolution();
                    solutionToAdd.setDescription(solutionDetails.get(0));
                    solutionToAdd.setId(Integer.parseInt(solutionDetails.get(1)));
                    solutionToAdd.saveToDB(connection.getConnection());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (userSolutionsManagementChoice.equals("view")) {
                try {
                    ArrayList<ExerciseSolution> usersSolutions = new ArrayList<ExerciseSolution>();
                    usersSolutions.addAll(ExerciseSolution.loadAllByUserId(connection.getConnection(), userId));
                    for (int i = 0; i < usersSolutions.size(); i++) {
                        System.out.println("Solution id: " +  usersSolutions.get(i).getId() + ", solution created: " + usersSolutions.get(i).getCreated() + ", solution updated: " + usersSolutions.get(i).getUpdated() + ", solutions description: " + usersSolutions.get(i).getDescription() + ", exercise id: " + usersSolutions.get(i).getExerciseId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                keepWorking = false;
            }
        }

        try {
            connection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int userId() {
        System.out.println("Podaj id użytkownika");
        Scanner scanner = new Scanner (System.in);
        boolean waitForInt = true;
        int userInput = -1;
        userInput = waitForUsersId(scanner, waitForInt, userInput);
        return userInput;
    }

    public static ArrayList<String> solutionAdding() {
        ArrayList<String> info = new ArrayList<String>();
//        ZoneId zoneId = ZoneId.of("Europe/Paris");
//        LocalDate today = LocalDate.now(zoneId);
//        String solutionCreatedDate = today.toString();
//        String solutionUpdatedDate = solutionCreatedDate;
//        info.add(solutionCreatedDate);
//        info.add(solutionUpdatedDate);
        System.out.println("Podaj opis rozwiązania");
        Scanner scanner = new Scanner(System.in);
        String solutionDescription = scanner.nextLine();
        info.add(solutionDescription);
        System.out.println("Podaj id zadania");
        boolean waitForInt = true;
        int userInput = -1;
        String exerciseId = Integer.toString(waitForUsersId(scanner, waitForInt, userInput));
        info.add(exerciseId);
        return info;
    }

    static int waitForUsersId(Scanner scanner, boolean waitForInt, int userInput) {
        while(waitForInt) {
            while (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("Id musi być liczbą całkowitą. Podaj id:");
            }
            userInput = scanner.nextInt();
            if (userInput < 1) {
                System.out.println("Id musi być liczbą całkowitą większą od 0. Podaj Id:");
            } else {
                waitForInt = false;
            }
        }
        return userInput;
    }


    public static String userChoiceSolutionsManagement() {
        Scanner scanner = new Scanner(System.in);
        boolean keepWorking = true;
        String userResponse = "";
        while (keepWorking) {
            userResponse = scanner.next();
            if (userResponse.equals("add") || userResponse.equals("view") || userResponse.equals("quit")) {
                keepWorking = false;
            } else {
                System.out.println("\nZŁY WYBÓR \nWybierz jedną z opcji:\n\nadd - dodanie rozwiązania,\nview - podgląd rozwiązań,\nquit - zakończenie programu.");
            }
        }
        return userResponse;
    }
}
