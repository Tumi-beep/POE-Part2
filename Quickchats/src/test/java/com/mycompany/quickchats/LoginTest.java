/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.quickchats;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author b7708
 */
public class LoginTest {
    
    public LoginTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCheckUserName() {
        Login login = new Login();
        
        // Pass: Contains an underscore and is <= 5 characters
        assertTrue(login.checkUserName("kyl_1"));
        
        // Fail: Missing an underscore
        assertFalse(login.checkUserName("kyle1"));
        
        // Fail: Contains underscore but too long (> 5 characters)
        assertFalse(login.checkUserName("kyle_smith"));
    }

    @Test
    public void testCheckPasswordComplexity() {
        Login login = new Login();
        
        // Pass: At least 8 characters, has uppercase, digit, and special character
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
        
        // Fail: Too short (< 8 characters)
        assertFalse(login.checkPasswordComplexity("A1!b2"));
        
        // Fail: Missing a special character
        assertFalse(login.checkPasswordComplexity("Password123"));
        
        // Fail: Missing a digit
        assertFalse(login.checkPasswordComplexity("Password!!!"));
    }

    @Test
    public void testCheckCellPhoneNumber() {
        Login login = new Login();
        
        // Pass: Matches SA international regex format (+27 followed by 9 digits)
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
        
        // Fail: Standard local format without country code
        assertFalse(login.checkCellPhoneNumber("0838968976"));
        
        // Fail: Missing the plus sign or wrong number of digits
        assertFalse(login.checkCellPhoneNumber("27838968976"));
    }

    @Test
    public void testRegisterUser() {
        Login login = new Login();
        
        // Test successful data match response
        String successResult = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(successResult.contains("Username successfully captured."));
        assertTrue(successResult.contains("Password successfully captured."));
        assertTrue(successResult.contains("Cell phone number successfully added."));
        
        // Test registration rejection due to bad username formatting
        String badUserResult = login.registerUser("kyle1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(badUserResult.contains("Username is not correctly formatted"));
    }

    @Test
    public void testLoginUser() {
        Login login = new Login();
        
        // Setup an existing registration framework footprint first
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        
        // Pass: Matching credentials match original variables exactly
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        
        // Fail: Incorrect password boundary entry
        assertFalse(login.loginUser("kyl_1", "WrongPassword1!"));
    }

    @Test
    public void testReturnLoginStatus() {
        Login login = new Login();
        
        // Verify true boolean execution routes to the custom welcome greeting response
        String successStatus = login.returnLoginStatus(true);
        assertEquals("Welcome Kyle, Smith it is great to see you again.", successStatus);
        
        // Verify false boolean execution routes to the rejection error notice
        String failureStatus = login.returnLoginStatus(false);
        assertEquals("Username or password incorrect, please try again.", failureStatus);
    }
    
}