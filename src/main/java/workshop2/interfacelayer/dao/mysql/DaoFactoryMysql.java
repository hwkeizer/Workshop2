/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.dao.mysql;

import workshop2.interfacelayer.dao.AccountDao;
import workshop2.interfacelayer.dao.AddressDao;
import workshop2.interfacelayer.dao.CustomerDao;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.OrderDao;
import workshop2.interfacelayer.dao.OrderItemDao;
import workshop2.interfacelayer.dao.ProductDao;

/**
 *
 * @author hwkei
 */
public class DaoFactoryMysql extends DaoFactory {
    
    @Override
    public AccountDao createAccountDao() {
        return new AccountDaoMysql();
    }

    @Override
    public CustomerDao createCustomerDao() {
        return new CustomerDaoMysql();
    }

    @Override
    public AddressDao createAddressDao() {
        return new AddressDaoMysql();
    }

    @Override
    public OrderDao createOrderDao() {
        return new OrderDaoMysql();
    }

    @Override
    public OrderItemDao createOrderItemDao() {
        return new OrderItemDaoMysql();
    }
    
    @Override
    public ProductDao createProductDao() {
        return new ProductDaoMysql();
    }
}
