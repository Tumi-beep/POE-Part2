/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject7;

/**
 *
 * @author b7708
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Login loginSystem = new Login();
    
    // Arrays / Lists defined by the requirements
    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();
    
    private static final String JSON_FILE = "stored_messages.json";

    public static void main(String[] args) {
        loadStoredMessagesFromJson(); // Load existing messages from JSON file on boot
        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("\n=== QuickChat System Menu ===");
            System.out.println("1) Register a new account");
            System.out.println("2) Login");
            System.out.println("3) Exit System");
            System.out.print("Selection: ");

            int choice;
            try { choice = Integer.parseInt(scanner.nextLine()); } 
            catch (NumberFormatException e) { System.out.println("Please enter a valid numeric option."); continue; }

            if (choice == 1) {
                handleRegistration();
            } else if (choice == 2) {
                if (handleLogin()) handleFeatureMenu(); 
            } else if (choice == 3) {
                System.out.println("Exiting QuickChat. Goodbye!");
                exitProgram = true;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private static void handleRegistration() { /* Same as your original code */
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

    private static boolean handleLogin() { /* Same as your original code */
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
        
        while (menuSelection != 3) {
            System.out.println("\n=== QuickChat Features ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Logout");
            System.out.println("4) Stored Messages Menu");
            System.out.print("Selection: ");
            
            try { menuSelection = Integer.parseInt(scanner.nextLine()); } 
            catch (NumberFormatException e) { System.out.println("Please enter a valid numeric option."); continue; }

            if (menuSelection == 1) {
                processMessages();
            } else if (menuSelection == 2) {
                displayRecentlySent();
            } else if (menuSelection == 3) {
                System.out.println("Logging out... Returning to Start Menu.");
            } else if (menuSelection == 4) {
                handleStoredMessagesMenu();
            } else {
                System.out.println("Invalid choice selection.");
            }
        }
    }

    private static void processMessages() {
        System.out.print("\nHow many messages would you like to enter? ");
        int targetCount;
        try { targetCount = Integer.parseInt(scanner.nextLine()); } 
        catch (NumberFormatException e) { System.out.println("Invalid count format."); return; }
        
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
            try { actionChoice = Integer.parseInt(scanner.nextLine()); } 
            catch (NumberFormatException e) { actionChoice = -1; }
            
            System.out.println(currentMsg.sentMessage(actionChoice));
            
            // Populate our Arrays / Lists based on the decision
            messageHashes.add(currentMsg.createMessageHash());
            messageIDs.add(currentMsg.getMessageId());
            
            if (actionChoice == 1) {
                sentMessages.add(currentMsg);
                System.out.println("\n--- Full Message Details ---");
                System.out.println(currentMsg.printMessages());
            } else if (actionChoice == 2) {
                disregardedMessages.add(currentMsg);
            } else if (actionChoice == 3) {
                storedMessages.add(currentMsg);
                saveToJsonFile(currentMsg);
            }
        }
    }

    private static void displayRecentlySent() {
        System.out.println("\n--- Recently Sent Messages ---");
        if (sentMessages.isEmpty()) { System.out.println("No messages sent yet."); return; }
        for (Message msg : sentMessages) {
            System.out.println("To: " + msg.getRecipientCell() + " | Msg: " + msg.getMessageBody());
        }
    }

    // ==========================================
    // MENU OPTION 4: STORED MESSAGES
    // ==========================================
    private static void handleStoredMessagesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Stored Messages Menu ===");
            System.out.println("A) Display sender and recipient of all stored messages");
            System.out.println("B) Display the longest stored message");
            System.out.println("C) Search for a message ID");
            System.out.println("D) Search for all messages stored for a particular recipient");
            System.out.println("E) Delete a message using the message hash");
            System.out.println("F) Display a report of all stored messages");
            System.out.println("G) Back to Features Menu");
            System.out.print("Selection: ");
            
            String choice = scanner.nextLine().toUpperCase();
            
            switch (choice) {
                case "A":
                    System.out.println("\n--- Senders and Recipients ---");
                    for (Message m : storedMessages) {
                        System.out.println("Sender: " + loginSystem.getRegisteredUsername() + " | Recipient: " + m.getRecipientCell());
                    }
                    break;
                case "B":
                    if (storedMessages.isEmpty()) { System.out.println("No stored messages."); break; }
                    Message longest = storedMessages.get(0);
                    for (Message m : storedMessages) {
                        if (m.getMessageBody().length() > longest.getMessageBody().length()) longest = m;
                    }
                    System.out.println("\n--- Longest Stored Message ---");
                    System.out.println("Message: " + longest.getMessageBody());
                    break;
                case "C":
                    System.out.print("Enter Message ID to search: ");
                    String searchId = scanner.nextLine();
                    boolean foundId = false;
                    for (Message m : storedMessages) {
                        if (m.getMessageId().equals(searchId)) {
                            System.out.println("Recipient: " + m.getRecipientCell() + " | Message: " + m.getMessageBody());
                            foundId = true;
                        }
                    }
                    if (!foundId) System.out.println("Message ID not found.");
                    break;
                case "D":
                    System.out.print("Enter Recipient Cell to search: ");
                    String searchCell = scanner.nextLine();
                    boolean foundCell = false;
                    for (Message m : storedMessages) {
                        if (m.getRecipientCell().equals(searchCell)) {
                            System.out.println("- " + m.getMessageBody());
                            foundCell = true;
                        }
                    }
                    if (!foundCell) System.out.println("No messages found for that recipient.");
                    break;
                case "E":
                    System.out.print("Enter Message Hash to delete: ");
                    String delHash = scanner.nextLine();
                    boolean deleted = storedMessages.removeIf(m -> m.createMessageHash().equals(delHash));
                    if (deleted) {
                        System.out.println("Message successfully deleted.");
                        rewriteJsonFile(); // Sync changes to the file
                    } else {
                        System.out.println("Hash not found.");
                    }
                    break;
                case "F":
                    System.out.println("\n--- Full Stored Messages Report ---");
                    for (Message m : storedMessages) {
                        System.out.println(m.printMessages());
                        System.out.println("-------------------------");
                    }
                    break;
                case "G":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    // ==========================================
    // JSON READ / WRITE HELPERS (No 3rd Party Libs)
    // ==========================================
    private static void saveToJsonFile(Message msg) {
        try (FileWriter fw = new FileWriter(JSON_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(msg.storeMessage() + ",");
        } catch (IOException e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    private static void rewriteJsonFile() {
        try (PrintWriter writer = new PrintWriter(new File(JSON_FILE))) {
            for (Message m : storedMessages) {
                writer.println(m.storeMessage() + ",");
            }
        } catch (IOException e) {
            System.out.println("Error updating JSON file: " + e.getMessage());
        }
    }

    private static void loadStoredMessagesFromJson() {
        File file = new File(JSON_FILE);
        if (!file.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
            String[] blocks = content.split("},");
            
            for (String block : blocks) {
                if (block.trim().isEmpty()) continue;
                
                String id = extractJsonValue(block, "messageId");
                String numStr = extractJsonValue(block, "messageNumber");
                int num = numStr.isEmpty() ? 0 : Integer.parseInt(numStr);
                String cell = extractJsonValue(block, "recipient");
                String body = extractJsonValue(block, "message");
                
                if (!id.isEmpty() && !body.isEmpty()) {
                    Message msg = new Message(id, num, cell, body);
                    storedMessages.add(msg);
                    messageHashes.add(msg.createMessageHash());
                    messageIDs.add(id);
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not read JSON file. Starting fresh.");
        }
    }

    private static String extractJsonValue(String jsonBlock, String key) {
        String searchKey = "\"" + key + "\": \"";
        int start = jsonBlock.indexOf(searchKey);
        if (start == -1) return "";
        start += searchKey.length();
        int end = jsonBlock.indexOf("\"", start);
        return (end == -1) ? "" : jsonBlock.substring(start, end);
    }
}