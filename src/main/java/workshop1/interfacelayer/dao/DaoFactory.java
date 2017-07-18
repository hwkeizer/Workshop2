/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop1.interfacelayer.dao;

import workshop1.interfacelayer.dao.mongo.DaoFactoryMongo;

/**
 *
 * @author hwkei
 */
public abstract class DaoFactory {
    
    // List of DAO types supported by this factory
    public static final int MYSQL = 1;
    public static final int MONGO = 2;
    
    // DAO's that can be produced. The concrete factories will implement
    // these methods.
    public abstract AccountDao createAccountDao();
    public abstract CustomerDao createCustomerDao();
    public abstract AddressDao createAddressDao();
    public abstract OrderDao createOrderDao();
    public abstract OrderItemDao createOrderItemDao();
    public abstract ProductDao createProductDao();
    
    // Deliver the correct factory based on the given factoryType
    public static DaoFactory getDaoFactory(int factoryType) {
        switch (factoryType) {
            case MYSQL :
                return new DaoFactoryMysql();
            case MONGO : {
                return new DaoFactoryMongo();
            }
            default :
                return null;
        }
    }

}
