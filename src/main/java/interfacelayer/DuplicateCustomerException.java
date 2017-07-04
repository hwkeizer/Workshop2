/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacelayer;

import java.sql.SQLException;

/**
 *
 * @author hwkei
 */
public class DuplicateCustomerException extends SQLException {
    
    public DuplicateCustomerException() {}
    public DuplicateCustomerException(String message) {
        super(message);
    }
}
