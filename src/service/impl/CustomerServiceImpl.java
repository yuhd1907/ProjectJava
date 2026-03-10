package service.impl;

import dao.ICustomerDAO;
import dao.impl.CustomerDAOImpl;
import model.Customer;
import service.ICustomerService;

import java.util.List;

public class CustomerServiceImpl implements ICustomerService {
    private static final ICustomerDAO customerDAO = new CustomerDAOImpl();

    // === Validation helpers ===

    /**
     * Kiểm tra số điện thoại hợp lệ: đúng 10 chữ số, bắt đầu bằng 0.
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("0\\d{9}");
    }

    /**
     * Kiểm tra email hợp lệ: phải kết thúc bằng @gmail.com.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.toLowerCase().endsWith("@gmail.com");
    }

    // === Business logic ===

    @Override
    public boolean existsByPhone(String phone, int excludeId) {
        Customer existing = customerDAO.findByPhone(phone);
        return existing != null && existing.getId() != excludeId;
    }

    @Override
    public List<Customer> findByName(String keyword) {
        return customerDAO.findByName(keyword);
    }

    // === CRUD ===

    @Override
    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    @Override
    public Customer findById(Integer id) {
        return customerDAO.findById(id);
    }

    @Override
    public boolean add(Customer customer) {
        return customerDAO.add(customer);
    }

    @Override
    public boolean update(Customer customer) {
        return customerDAO.update(customer);
    }

    @Override
    public boolean delete(Integer id) {
        return customerDAO.delete(id);
    }
}
