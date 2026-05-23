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
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCheckMessageID() {
        // Test a valid ID that is equal to or under 10 characters
        Message validMsg = new Message("0012345678", 0, "+27718693002", "Hello World");
        assertTrue(validMsg.checkMessageID());

        // Test an invalid ID that breaches the 10 character boundary limit
        Message invalidMsg = new Message("123456789012345", 0, "+27718693002", "Hello World");
        assertFalse(invalidMsg.checkMessageID());
    }

    @Test
    public void testCheckRecipientCell() {
        Message testMsg = new Message("0012345678", 0, "+27718693002", "Hello World");
        
        // Verify valid SA international formatting passes
        String validResult = testMsg.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", validResult);

        // Verify invalid formatting returns the descriptive error message
        String invalidResult = testMsg.checkRecipientCell("0718693002");
        assertTrue(invalidResult.contains("incorrectly formatted"));
    }

    @Test
    public void testCheckMessageLength() {
        Message testMsg = new Message("0012345678", 0, "+27718693002", "Short text");
        
        // Under limit test
        assertEquals("Message ready to send.", testMsg.checkMessageLength("Short clean message text."));

        // Over limit test (257 characters total, which exceeds 250 by exactly 7 characters)
        String longText = "This string is specifically engineered to test our overflow metric monitoring configuration rules. It works by expanding the body text length past the normal boundary of two hundred and fifty characters, forcing a systematic length validation error return: 1234567";
        assertEquals("Message exceeds 250 characters by 7; please reduce the size.", testMsg.checkMessageLength(longText));
    }

    @Test
    public void testCreateMessageHash() {
        // Configuration: ID first 2 chars = "00", index = 1, first word = "Good", last word = "friend!" (cleaned to "friend")
        Message testMsg = new Message("0047859632", 1, "+27718693002", "Good morning my friend!");
        
        // Should compile into a completely upper-case token string "00:1:GOODFRIEND"
        assertEquals("00:1:GOODFRIEND", testMsg.createMessageHash());
    }

    @Test
    public void testSentMessage() {
        Message testMsg = new Message("0012345678", 0, "+27718693002", "Hello");
        
        assertEquals("Message successfully sent.", testMsg.sentMessage(1));
        assertEquals("Press 0 to delete the message.", testMsg.sentMessage(2));
        assertEquals("Message successfully stored.", testMsg.sentMessage(3));
    }

    @Test
    public void testPrintMessages() {
        Message testMsg = new Message("0012345678", 0, "+27718693002", "Hello World");
        String printedDetails = testMsg.printMessages();
        
        // Verify that the output layout prints all essential metadata attributes
        assertTrue(printedDetails.contains("Message ID: 0012345678"));
        assertTrue(printedDetails.contains("Message Hash:"));
        assertTrue(printedDetails.contains("Recipient: +27718693002"));
        assertTrue(printedDetails.contains("Message: Hello World"));
    }

    @Test
    public void testReturnTotalMessages() {
        int baseCount = Message.returnTotalMessages();
        Message testMsg = new Message("0012345678", 0, "+27718693002", "Testing tracker");
        
        // Trigger option 1 to count an execution loop as a completed transmission
        testMsg.sentMessage(1);
        testMsg.sentMessage(1);
        
        assertEquals(baseCount + 2, Message.returnTotalMessages());
    }

    @Test
    public void testStoreMessage() {
        Message testMsg = new Message("0012345678", 2, "+27718693002", "JSON Check");
        String jsonOutput = testMsg.storeMessage();
        
        // Confirm that the storage file template generator outputs true formatted JSON tokens
        assertTrue(jsonOutput.startsWith("{"));
        assertTrue(jsonOutput.contains("\"messageId\": \"0012345678\""));
        assertTrue(jsonOutput.contains("\"recipient\": \"+27718693002\""));
        assertTrue(jsonOutput.endsWith("}"));
    }
}