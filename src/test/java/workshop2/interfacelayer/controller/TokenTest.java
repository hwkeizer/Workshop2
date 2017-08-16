/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import workshop2.interfacelayer.controller.Token;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hwkei
 */
public class TokenTest {
    
    public TokenTest() {
    }

    /**
     * Test of createJWT method, of class Token.
     */
    @Test
    public void testCreateJWT() {
        System.out.println("createJWT");
        // Create a token
        String token;
        String id = "TestID";
        String issuer = "TestIssuer";
        String subject = "TestSubject";
        long expireTime = 0;
        token = Token.createJWT(id, issuer, subject, expireTime);
        assertEquals("Correct id should be retrieved", id, Token.parseJWT(token));
    }
    
    /**
     * Test of createJWT method, of class Token.
     */
    @Test
    public void testExpiredJWT() {
        System.out.println("createExpiredJWT");
        // Create a token
        String token;
        String id = "TestID";
        String issuer = "TestIssuer";
        String subject = "TestSubject";
        long expireTime = 1;
        token = Token.createJWT(id, issuer, subject, expireTime);
        assertEquals("Expired id should return null", null, Token.parseJWT(token));
    }
    
}
