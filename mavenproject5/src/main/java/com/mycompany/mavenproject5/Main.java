/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject5;

/**
 *
 * @author b7708
 */
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Login loginSystem = new Login();

    public static void main(String[] args) {
        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("\n=== QuickChat System Menu ===");
            System.out.println("1) Register a new account");
            System.out.println("2) Login");
            System.out.println("3) Exit System");
            System.out.print("Selection: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric option.");
                continue;
            }

            if (choice == 1) {
                handleRegistration();
            } else if (choice == 2) {
                boolean loggedIn = handleLogin();
                if (loggedIn) {
                    handleFeatureMenu(); 
                }
            } else if (choice == 3) {
                System.out.println("Exiting QuickChat. Goodbye!");
                exitProgram = true;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private static void handleRegistration() {
        System.out.println("\n--- Welcome to QuickChat Account Registration ---");
        boolean isRegistered = false;
        
        while (!isRegistered) {
            System.out.print("Enter Username: ");
            String user = scanner.nextLine();
            System.out.print("Enter Password: ");
            String pass = scanner.nextLine();
            System.out.print("Enter SA Mobile Number (e.g., +27838968976): ");
            String cell = scanner.nextLine();
            
            String regOutput = loginSystem.registerUser(user, pass, cell);
            System.out.println("\n" + regOutput + "\n");
            
            if (regOutput.contains("Cell phone number successfully added")) {
                isRegistered = true;
                System.out.println("Returning to main menu...\n");
            }
        }
    }

    private static boolean handleLogin() {
        System.out.println("\n--- Account Login ---");
        System.out.print("Enter Username: ");
        String user = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pass = scanner.nextLine();
        
        boolean loggedIn = loginSystem.loginUser(user, pass);
        System.out.println("\n" + loginSystem.returnLoginStatus(loggedIn));
        return loggedIn;
    }

    private static void handleFeatureMenu() {
        int menuSelection = 0;
        System.out.println("\nWelcome to QuickChat Features.");
        
        while (menuSelection != 3) {
            System.out.println("\nPlease choose one of the following options:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Logout");
            System.out.print("Selection: ");
            
            try {
                menuSelection = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric option.");
                continue;
            }

            if (menuSelection == 1) {
                processMessages();
            } else if (menuSelection == 2) {
                System.out.println("Coming Soon.");
            } else if (menuSelection == 3) {
                System.out.println("Logging out... Returning to Start Menu.");
            } else {
                System.out.println("Invalid choice selection.");
            }
        }
    }

    private static void processMessages() {
        System.out.print("\nHow many messages would you like to enter? ");
        int targetCount;
        try {
            targetCount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid count format.");
            return;
        }
        
        for (int i = 0; i < targetCount; i++) {
            String generatedId = String.format("%010d", (long)(Math.random() * 10000000000L));
            System.out.println("\n--- Processing Message [" + (i + 1) + " of " + targetCount + "] ---");
            System.out.println("Message ID generated: " + generatedId);
            
            System.out.print("Enter Recipient Cell Number: ");
            String recipient = scanner.nextLine();
            
            System.out.print("Enter Message (Max 250 characters): ");
            String body = scanner.nextLine();
            
            Message currentMsg = new Message(generatedId, i, recipient, body);
            
            String lengthCheck = currentMsg.checkMessageLength(body);
            if (lengthCheck.contains("exceeds")) {
                System.out.println(lengthCheck);
                i--; 
                continue;
            }
            
            System.out.println("\nSelect an action for this message:");
            System.out.println("1 - Send Message");
            System.out.println("2 - Disregard Message");
            System.out.println("3 - Store Message to send later");
            System.out.print("Action choice: ");
            
            int actionChoice;
            try {
                actionChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                actionChoice = -1;
            }
            
            System.out.println(currentMsg.sentMessage(actionChoice));
            
            if (actionChoice == 1) {
                System.out.println("\n--- Full Message Details ---");
                System.out.println(currentMsg.printMessages());
            } else if (actionChoice == 3) {
                System.out.println("\n--- JSON File Storage Preview ---");
                String rawJson = currentMsg.storeMessage();
                System.out.println(rawJson);
                
                // Write the raw JSON directly to our local file
                saveJsonToFile(rawJson);
            }
        }
        System.out.println("\nTotal successful messages sent during session: " + Message.returnTotalMessages());
    }

    // Handles the actual file writing to your hard drive
    private static void saveJsonToFile(String jsonContent) {
        try (FileWriter fw = new FileWriter("messages.json", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            bw.write(jsonContent);
            bw.newLine(); 
            bw.write(",\n"); // Prepares the file for the next JSON entry
            
            System.out.println(">> Success: Data written securely to 'messages.json'.");
        } catch (IOException e) {
            System.out.println(">> File Output Error: Unable to complete write process. " + e.getMessage());
        }
    }
}