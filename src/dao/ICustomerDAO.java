package dao;

import model.Customer;

public interface ICustomerDAO extends IBaseDAO<Customer, Integer> {
    // Kế thừa toàn bộ CRUD từ IBaseDAO
    // Có thể bổ sung thêm phương thức đặc thù cho Customer ở đây nếu cần
}
