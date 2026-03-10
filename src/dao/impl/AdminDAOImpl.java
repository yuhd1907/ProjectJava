package dao.impl;

import dao.IAdminDAO;
import model.Admin;
import utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImpl implements IAdminDAO {
    @Override
    public Admin checkUsername(String username) {
        String sql = "SELECT * FROM ADMIN WHERE USERNAME=?";
        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement pre = conn.prepareStatement(sql);
        ) {
            pre.setString(1, username);
            ResultSet rs = pre.executeQuery();
            if(rs.next()){
                return new Admin(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
