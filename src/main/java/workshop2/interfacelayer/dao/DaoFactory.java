/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao;

import workshop2.interfacelayer.dao.mysql.DaoFactoryMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.mongo.DaoFactoryMongo;

/**
 *
 * @author hwkei
 */
public abstract class DaoFactory {
    private static final Logger log = LoggerFactory.getLogger(DaoFactory.class);
    private static String databaseType;
//    public static void setDatabaseType(int dummyType) {
//        if(dummyType == 0) {
//            log.debug("databaseType before trying setter method is: " + databaseType);
//            databaseType = 
//            log.debug("databaseType after trying setter method is: " + databaseType);
//        }
//        else
//            databaseType = dummyType;
//    }
    
    
    
    // DAO's that can be produced. The concrete factories will implement
    // these methods.
    public abstract AccountDao createAccountDao();
    public abstract CustomerDao createCustomerDao();
    public abstract AddressDao createAddressDao();
    public abstract OrderDao createOrderDao();
    public abstract OrderItemDao createOrderItemDao();
    public abstract ProductDao createProductDao();
    
    // Deliver the correct factory based on the current databse type
    public static DaoFactory getDaoFactory() {
        databaseType = DatabaseConnection.getInstance().getDatabaseType();
        log.debug("DatabaseType: {}", databaseType);
        switch (databaseType) {
            case "MYSQL" :                
                return new DaoFactoryMysql();
            case "MONGO" : {
                return new DaoFactoryMongo();
            }
            default :
                return null;
        }
    }

}