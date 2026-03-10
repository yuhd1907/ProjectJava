package service.impl;

import dao.IInvoiceDAO;
import dao.impl.InvoiceDAOImpl;
import model.Invoice;
import service.IInvoiceService;

import java.util.List;

public class InvoiceServiceImpl implements IInvoiceService {
    private static final IInvoiceDAO invoiceDAO = new InvoiceDAOImpl();

    @Override
    public void add(Invoice invoice) {
        invoiceDAO.add(invoice);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceDAO.findAll();
    }

    @Override
    public Invoice findById(Integer id) {
        return invoiceDAO.findById(id);
    }

    @Override
    public void update(Invoice invoice) {
        invoiceDAO.update(invoice);
    }

    @Override
    public void delete(Integer id) {
        invoiceDAO.delete(id);
    }

    @Override
    public List<Invoice> findByCustomerId(int customerId) {
        return invoiceDAO.findByCustomerId(customerId);
    }

    @Override
    public List<Invoice> findByDate(String date) {
        return invoiceDAO.findByDate(date);
    }

    @Override
    public double revenueByDay(String date) {
        return invoiceDAO.revenueByDay(date);
    }

    @Override
    public double revenueByMonth(int month, int year) {
        return invoiceDAO.revenueByMonth(month, year);
    }

    @Override
    public double revenueByYear(int year) {
        return invoiceDAO.revenueByYear(year);
    }
}
