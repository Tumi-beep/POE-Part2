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
public class MessageTest {
    private Message validMessageObject;
    
    public MessageTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        public void setUp() {
        // Construct a clean, predictable message setup instance before running each test case
        validMessageObject = new Message(
            "0012345678", 
            0, 
            "+27718693002", 
            "Hi Mike, can you join us for dinner tonight?"
        );
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCheckMessageID() {
    }

    @Test
    public void testCheckRecipientCell() {
    }

    @Test
    public void testCheckMessageLength() {
    }

    @Test
    public void testCreateMessageHash() {
    }

    @Test
    public void testSentMessage() {
    }

    @Test
    public void testPrintMessages() {
    }

    @Test
    public void testReturnTotalMessages() {
    }

    @Test
    public void testStoreMessage() {
    }
    
}
