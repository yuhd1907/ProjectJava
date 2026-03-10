package dao;

import model.InvoiceDetail;
import java.util.List;

public interface IInvoiceDetailDAO extends IBaseDAO<InvoiceDetail, Integer> {
    List<InvoiceDetail> findByInvoiceId(int invoiceId);
}
