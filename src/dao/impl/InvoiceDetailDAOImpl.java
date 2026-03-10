package dao.impl;

import dao.IInvoiceDetailDAO;
import model.InvoiceDetail;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAOImpl implements IInvoiceDetailDAO {

    private InvoiceDetail mapRow(ResultSet rs) throws SQLException {
        return new InvoiceDetail(
                rs.getInt("id"),
                rs.getInt("invoice_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getDouble("unit_price"));
    }

    @Override
    public void add(InvoiceDetail detail) {
        String sql = "INSERT INTO invoice_detail(invoice_id, product_id, quantity, unit_price) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getInvoiceId());
            ps.setInt(2, detail.getProductId());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getUnitPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InvoiceDetail findById(Integer id) {
        String sql = "SELECT * FROM invoice_detail WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(InvoiceDetail detail) {
        throw new UnsupportedOperationException("Không hỗ trợ cập nhật chi tiết hóa đơn.");
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Không hỗ trợ xóa chi tiết hóa đơn.");
    }

    @Override
    public List<InvoiceDetail> findAll() {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<InvoiceDetail> findByInvoiceId(int invoiceId) {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail WHERE invoice_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
