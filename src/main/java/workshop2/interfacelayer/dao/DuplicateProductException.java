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
public class DuplicateProductException extends SQLException {
    
    public DuplicateProductException() {}
    public DuplicateProductException(String message) {
        super(message);
    }
}
