package service.impl;

import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.Admin;
import service.IAdminService;

public class AdminServiceImpl implements IAdminService {
    private static final IAdminDAO adminDAO = new AdminDAOImpl();

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminDAO.checkUsername(username);
        if(admin != null &&  admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}
