/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hwkei
 */
public class PasswordHash {
    private static final Logger log = LoggerFactory.getLogger(PasswordHash.class);
    private static final int ITERATIONS = 1000; // Number of iterations should depend on future hardware development
    private static final int KEY_LENGTH = 64*8; // Key length can be adjusted if required
    
    /**
     * generates a new password hash based on the given password
     * @param password
     * @return hash
     */
    public static String generateHash(String password) {
        try {
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();        
        
            // generateHash the raw key material
            PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_LENGTH);
            // Create the SecretKeyFactory with the specified algorithm
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            // generateHash the hash
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return ITERATIONS + ":" + hexFromBytes(salt) + ":" + hexFromBytes(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("Ernstige fout bij de wachtwoordverwerking. Raadpleeg svp een beheerder!");
        }
        return null;
    }
    
    /**
     * Validates a password against the stored hash
     * @param originalPassword
     * @param storedHash
     * @return boolean
     */
    public static boolean validatePassword(String originalPassword, String storedHash)  {
        // Split the stored hash back in its composing components
        String[] parts = storedHash.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = bytesFromHex(parts[1]);
        byte[] hash = bytesFromHex(parts[2]);
        boolean valid = false;
//        int diff = -1;
        
        // generateHash the raw key material
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        // Create the SecretKeyFactory with the specified algorithm
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            // generateHash the hash
            byte[] testHash = skf.generateSecret(spec).getEncoded();
            // Compare the new generated hash with the stored hash
            valid = Arrays.equals(hash, testHash);      
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("Ernstige fout bij de wachtwoordverwerking. Raadpleeg svp een beheerder!");
        }
        return valid;
    }
    
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    private static String hexFromBytes(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    
    private static byte[] bytesFromHex(String hex) {
        byte[] bytes = new byte[hex.length()/2];
        for(int i = 0; i < bytes.length ; i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
