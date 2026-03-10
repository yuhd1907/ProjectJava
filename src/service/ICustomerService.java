package service;

import model.Customer;

import java.util.List;

public interface ICustomerService extends IBaseService<Customer, Integer> {
    List<Customer> findByName(String keyword);

    boolean existsByPhone(String phone, int excludeId);
}
