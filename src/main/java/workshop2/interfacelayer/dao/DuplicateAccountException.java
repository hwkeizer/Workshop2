/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao;

import java.sql.SQLException;

/**
 *
 * @author hwkei
 */
public class DuplicateAccountException extends SQLException {
    
    public DuplicateAccountException() {}
    public DuplicateAccountException(String message) {
        super(message);
    }
}
