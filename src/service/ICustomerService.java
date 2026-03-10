package service;

import model.Customer;

public interface ICustomerService extends IBaseService<Customer, Integer> {
    // Kế thừa toàn bộ CRUD từ IBaseService
    // Có thể bổ sung thêm phương thức đặc thù cho Customer ở đây nếu cần
}
