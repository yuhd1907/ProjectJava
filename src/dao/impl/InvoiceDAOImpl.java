package dao.impl;

import dao.IInvoiceDAO;
import model.Invoice;
import utils.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl implements IInvoiceDAO {

    private Invoice mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
        return new Invoice(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                createdAt,
                rs.getDouble("total_amount"));
    }

    @Override
    public void add(Invoice invoice) {
        String sql = "INSERT INTO invoice(customer_id, created_at, total_amount) VALUES(?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoice.getCustomerId());
            ps.setTimestamp(2, Timestamp.valueOf(
                    invoice.getCreatedAt() != null ? invoice.getCreatedAt() : LocalDateTime.now()));
            ps.setDouble(3, invoice.getTotalAmount());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Invoice findById(Integer id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
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
    public void update(Invoice invoice) {
        throw new UnsupportedOperationException("Không hỗ trợ cập nhật hóa đơn.");
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Không hỗ trợ xóa hóa đơn.");
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice ORDER BY created_at DESC, id DESC";
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
    public List<Invoice> findByCustomerId(int customerId) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE customer_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Invoice> findByDate(String date) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE CAST(created_at AS DATE) = CAST(? AS DATE) ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double revenueByDay(String date) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM invoice WHERE CAST(created_at AS DATE) = CAST(? AS DATE)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double revenueByMonth(int month, int year) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM invoice WHERE EXTRACT(MONTH FROM created_at)=? AND EXTRACT(YEAR FROM created_at)=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double revenueByYear(int year) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM invoice WHERE EXTRACT(YEAR FROM created_at)=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
