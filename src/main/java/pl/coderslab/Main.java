package pl.coderslab;


import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class TaskManager {

    static String[][] tasks;
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};

    public static void main(String[] args) {
        tasks = loadFile(FILE_NAME);
        printOptions(OPTIONS);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    exit();
                    System.exit(0);
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.err.println("Choose a valid option:");
            }
            printOptions(OPTIONS);
        }
    }

    public static void printOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
        System.out.println();
    }

    public static String[][] loadFile(String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        String[][] tab = null;
        try {
            List<String> lines = Files.readAllLines(path);
            tab = new String[lines.size()][lines.get(0).split(",").length];

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                for (int j = 0; j < parts.length; j++) {
                    tab[i][j] = parts[j];
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
        }
        return tab;
    }

    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter task description:");
        String description = scanner.nextLine();
        System.out.println("Enter task's due date:");
        String dueDate = scanner.nextLine();
        System.out.println("Enter task's priority (true/false):");
        String priority = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = priority;
    }

    private static void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of task to be removed:");

        while (!scanner.hasNextInt()) {
            System.err.println("Make sure to enter an integer, try again:");
            scanner.next();
        }

        int input = scanner.nextInt();
        while (input >= tasks.length) {
            System.err.println("Make sure to enter a number less than " + tasks.length);
            input = scanner.nextInt();
        }

        tasks = ArrayUtils.remove(tasks, input);
    }

    public static void exit() {
        System.out.println(ConsoleColors.RED + "Bye!");
        Path path = Paths.get(FILE_NAME);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < tasks.length; i++) {
            for (int j = 0; j < tasks[i].length; j++) {
                if (j == tasks[i].length - 1) {
                    content.append(tasks[i][j]).append("\n");
                } else content.append(tasks[i][j]).append(", ");
            }
            try {
                Files.writeString(path, content.toString());
            } catch (IOException e) {
                System.out.print("Could not create and / or write to the file: " + path);
            }
        }

    }

}