package dao;

import model.Customer;

import java.util.List;

public interface ICustomerDAO extends IBaseDAO<Customer, Integer> {
    Customer findByPhone(String phone);

    List<Customer> findByName(String keyword);
}
