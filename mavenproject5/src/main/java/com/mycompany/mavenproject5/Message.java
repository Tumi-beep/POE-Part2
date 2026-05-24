/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject5;

/**
 *
 * @author b7708
 */
public class Message {
    private final String messageId;
    private final int messageNumber;
    private final String recipientCell;
    private final String messageBody;
    
    private static int totalMessagesSent = 0;

    public Message(String messageId, int messageNumber, String recipientCell, String messageBody) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageBody = messageBody;
    }

    public boolean checkMessageID() {
        return this.messageId != null && this.messageId.length() <= 10;
    }

    public String checkRecipientCell(String cellNumber) {
        // Borrows the phone number validation from the Login class
        Login loginChecker = new Login();
        if (loginChecker.checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength(String message) {
        if (message == null) return "Message cannot be empty.";
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int exceededBy = message.length() - 250;
            return "Message exceeds 250 characters by " + exceededBy + "; please reduce the size.";
        }
    }

    public String createMessageHash() {
        if (messageId == null || messageId.length() < 2 || messageBody == null || messageBody.trim().isEmpty()) {
            return "00:0:UNKNOWN";
        }
        
        String firstTwoId = messageId.substring(0, 2);
        String[] words = messageBody.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        return (firstTwoId + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid selection.";
        }
    }

    public String printMessages() {
        return "Message ID: " + messageId + "\nMessage Hash: " + createMessageHash() + 
               "\nRecipient: " + recipientCell + "\nMessage: " + messageBody;
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Manual JSON construction
    public String storeMessage() {
        String cleanBody = messageBody.replace("\"", "\\\""); // Prevents quotes from breaking JSON syntax
        
        return "{\n" +
               "  \"messageId\": \"" + messageId + "\",\n" +
               "  \"messageHash\": \"" + createMessageHash() + "\",\n" +
               "  \"recipient\": \"" + recipientCell + "\",\n" +
               "  \"message\": \"" + cleanBody + "\"\n" +
               "}";
    }
}
