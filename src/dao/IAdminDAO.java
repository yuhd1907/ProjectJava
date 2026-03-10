package dao;

import model.Admin;

public interface IAdminDAO {
    Admin checkUsername(String username);
}
