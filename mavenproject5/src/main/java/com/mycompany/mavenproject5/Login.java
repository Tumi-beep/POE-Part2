/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject5;

/**
 *
 * @author b7708
 */
import java.util.regex.Pattern;

public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellNumber;
    private final String firstName = "Kyle"; 
    private final String lastName = "Smith";   

    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null) return false;
        String regex = "^\\+27\\d{9}$";
        return Pattern.matches(regex, cellNumber);
    }

    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellNumber = cellNumber;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public boolean loginUser(String username, String password) {
        return username != null && password != null && 
               username.equals(this.registeredUsername) && 
               password.equals(this.registeredPassword);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}