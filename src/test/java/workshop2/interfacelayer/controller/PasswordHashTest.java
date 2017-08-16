/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import workshop2.interfacelayer.controller.PasswordHash;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hwkei
 */
public class PasswordHashTest {
    
    public PasswordHashTest() {
    }

    /**
     * Test of validatePassword method, of class PasswordHash.
     * @throws java.lang.Exception
     */
    @Test
    public void testValidateCorrectPassword() throws Exception {
        System.out.println("ValidationCorrectPassword");
        String originalPassword = "welkom01";
        String generatedSecurePasswordHash = PasswordHash.generateHash(originalPassword);
        System.out.println(generatedSecurePasswordHash);
        assertTrue("Correct password should validate correct", PasswordHash.validatePassword(originalPassword, generatedSecurePasswordHash));
    }
    
    /**
     * Test of validatePassword method, of class PasswordHash.
     * @throws java.lang.Exception
     */
    @Test
    public void testValidateIncorrectPassword() throws Exception {
        System.out.println("ValidationIncorrectPassword");
        String originalPassword = "welkom01";
        String wrongPassword = "welkom02";
        String generatedSecurePasswordHash = PasswordHash.generateHash(originalPassword);
        System.out.println(generatedSecurePasswordHash);
        assertFalse("Incorrect password should not validate correct", PasswordHash.validatePassword(wrongPassword, generatedSecurePasswordHash));
    }    
}
