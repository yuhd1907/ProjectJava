package service;

import model.Invoice;
import java.util.List;

public interface IInvoiceService extends IBaseService<Invoice, Integer> {
    List<Invoice> findByCustomerId(int customerId);

    List<Invoice> findByDate(String date);

    double revenueByDay(String date);

    double revenueByMonth(int month, int year);

    double revenueByYear(int year);
}
