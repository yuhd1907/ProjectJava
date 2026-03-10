package dao;

import model.Invoice;
import java.util.List;

public interface IInvoiceDAO extends IBaseDAO<Invoice, Integer> {
    // Tìm kiếm theo khách hàng
    List<Invoice> findByCustomerId(int customerId);

    // Tìm kiếm theo ngày (yyyy-MM-dd)
    List<Invoice> findByDate(String date);

    // Thống kê doanh thu
    double revenueByDay(String date);

    double revenueByMonth(int month, int year);

    double revenueByYear(int year);
}
