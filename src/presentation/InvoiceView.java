package presentation;

import dao.impl.InvoiceDetailDAOImpl;
import model.Customer;
import model.Invoice;
import model.InvoiceDetail;
import model.Product;
import service.ICustomerService;
import service.IInvoiceService;
import service.IProductService;
import service.impl.CustomerServiceImpl;
import service.impl.InvoiceServiceImpl;
import service.impl.ProductServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class InvoiceView {
    private static final IInvoiceService invoiceService = new InvoiceServiceImpl();
    private static final IProductService productService = new ProductServiceImpl();
    private static final ICustomerService customerService = new CustomerServiceImpl();
    private static final InvoiceDetailDAOImpl invoiceDetailDAO = new InvoiceDetailDAOImpl();

    public static void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========= QUẢN LÝ HÓA ĐƠN & THỐNG KÊ =========");
            System.out.println("1. Thêm hóa đơn mới");
            System.out.println("2. Hiển thị danh sách hóa đơn");
            System.out.println("3. Tìm hóa đơn theo khách hàng");
            System.out.println("4. Tìm hóa đơn theo ngày");
            System.out.println("5. Thống kê doanh thu");
            System.out.println("6. Quay lại menu chính");
            System.out.println("================================================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> addInvoice(scanner);
                    case 2 -> showAll(scanner);
                    case 3 -> findByCustomer(scanner);
                    case 4 -> findByDate(scanner);
                    case 5 -> showRevenueMenu(scanner);
                    case 6 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    // ==================== THÊM HÓA ĐƠN ====================

    private static void addInvoice(Scanner scanner) {
        System.out.println("\n--- THÊM HÓA ĐƠN MỚI ---");
        try {
            // Chọn khách hàng
            List<Customer> customers = customerService.findAll();
            if (customers.isEmpty()) {
                System.out.println("Chưa có khách hàng. Vui lòng thêm khách hàng trước.");
                return;
            }
            System.out.println("== Danh sách khách hàng ==");
            customers.forEach(c -> System.out.printf("  [%d] %s - %s%n", c.getId(), c.getName(), c.getPhone()));
            System.out.print("Chọn ID khách hàng: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            Customer customer = customerService.findById(customerId);
            if (customer == null) {
                System.out.println("Không tìm thấy khách hàng.");
                return;
            }

            // Chọn sản phẩm
            List<Product> products = productService.findAll();
            if (products.isEmpty()) {
                System.out.println("Chưa có sản phẩm.");
                return;
            }
            System.out.println("\n== Danh sách sản phẩm ==");
            products.forEach(p -> System.out.printf("  [%d] %-25s | %,.0f VND | Tồn: %d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getStock()));
            System.out.print("Chọn ID sản phẩm: ");
            int productId = Integer.parseInt(scanner.nextLine().trim());
            Product product = productService.findById(productId);
            if (product == null) {
                System.out.println("Không tìm thấy sản phẩm.");
                return;
            }

            System.out.print("Số lượng: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            if (quantity <= 0) {
                System.out.println("Số lượng phải lớn hơn 0.");
                return;
            }
            if (quantity > product.getStock()) {
                System.out.printf("Tồn kho không đủ! Chỉ còn %d sản phẩm.%n", product.getStock());
                return;
            }

            double unitPrice = product.getPrice();
            double totalAmount = unitPrice * quantity;
            System.out.printf("Đơn giá   : %,.0f VND%n", unitPrice);
            System.out.printf("Tổng tiền : %,.0f VND%n", totalAmount);
            System.out.print("Xác nhận tạo hóa đơn? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                System.out.println("Đã hủy.");
                return;
            }

            // Lưu invoice (header)
            Invoice invoice = new Invoice(0, customerId, LocalDateTime.now(), totalAmount);
            invoiceService.add(invoice);

            // Lấy invoice_id vừa tạo
            List<Invoice> invoices = invoiceService.findByCustomerId(customerId);
            if (invoices.isEmpty()) {
                System.out.println("Lỗi khi lấy hóa đơn vừa tạo.");
                return;
            }
            int newInvoiceId = invoices.get(0).getId();

            // Lưu invoice_detail
            invoiceDetailDAO.add(new InvoiceDetail(0, newInvoiceId, productId, quantity, unitPrice));

            // Cập nhật tồn kho
            product.setStock(product.getStock() - quantity);
            productService.update(product);

            System.out.println("✓ Tạo hóa đơn thành công! (ID: " + newInvoiceId + ")");
        } catch (NumberFormatException e) {
            System.out.println("Giá trị nhập không hợp lệ.");
        }
    }

    // ==================== HIỂN THỊ ====================

    private static void showAll(Scanner scanner) {
        List<Invoice> list = invoiceService.findAll();
        if (list.isEmpty()) {
            System.out.println("Không có hóa đơn nào.");
            return;
        }
        printHeader();
        list.forEach(System.out::println);
        printDivider();
        System.out.println("  Tổng cộng: " + list.size() + " hóa đơn.");
        System.out.print("\nXem chi tiết hóa đơn? Nhập ID (hoặc 0 để bỏ qua): ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (id > 0)
                showDetail(id);
        } catch (NumberFormatException e) {
            /* bỏ qua */ }
    }

    private static void showDetail(int invoiceId) {
        List<InvoiceDetail> details = invoiceDetailDAO.findByInvoiceId(invoiceId);
        if (details.isEmpty()) {
            System.out.println("Không có chi tiết cho hóa đơn #" + invoiceId);
            return;
        }
        System.out.println("\n--- CHI TIẾT HÓA ĐƠN #" + invoiceId + " ---");
        System.out.println("+------------+-----+------------------+------------------+");
        System.out.printf("| %-10s | %-3s | %16s | %16s |%n", "ID Sản phẩm", "SL", "Đơn giá (VND)", "Thành tiền (VND)");
        System.out.println("+------------+-----+------------------+------------------+");
        details.forEach(System.out::println);
        System.out.println("+------------+-----+------------------+------------------+");
    }

    private static void printHeader() {
        printDivider();
        System.out.printf("| %-4s | %-13s | %-19s | %18s |%n",
                "ID", "Khách hàng ID", "Thời gian tạo", "Tổng tiền (VND)");
        printDivider();
    }

    private static void printDivider() {
        System.out.println("+------+---------------+---------------------+--------------------+");
    }

    private static void printResult(List<Invoice> list) {
        if (list.isEmpty())
            System.out.println("Không tìm thấy hóa đơn phù hợp.");
        else {
            printHeader();
            list.forEach(System.out::println);
            printDivider();
        }
    }

    // ==================== TÌM KIẾM ====================

    private static void findByCustomer(Scanner scanner) {
        System.out.println("\n--- TÌM HÓA ĐƠN THEO KHÁCH HÀNG ---");
        customerService.findAll().forEach(c -> System.out.printf("  [%d] %s%n", c.getId(), c.getName()));
        System.out.print("Nhập ID khách hàng: ");
        try {
            printResult(invoiceService.findByCustomerId(Integer.parseInt(scanner.nextLine().trim())));
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        }
    }

    private static void findByDate(Scanner scanner) {
        System.out.println("\n--- TÌM HÓA ĐƠN THEO NGÀY ---");
        System.out.print("Nhập ngày (yyyy-MM-dd): ");
        printResult(invoiceService.findByDate(scanner.nextLine().trim()));
    }

    // ==================== THỐNG KÊ ====================

    public static void showRevenueMenu(Scanner scanner) {
        System.out.println("\n--- THỐNG KÊ DOANH THU ---");
        System.out.println("1. Theo ngày (yyyy-MM-dd)");
        System.out.println("2. Theo tháng");
        System.out.println("3. Theo năm");
        System.out.print("Nhập lựa chọn: ");
        try {
            switch (Integer.parseInt(scanner.nextLine().trim())) {
                case 1 -> {
                    System.out.print("Nhập ngày (yyyy-MM-dd): ");
                    String d = scanner.nextLine().trim();
                    System.out.printf("Doanh thu ngày %s: %,.0f VND%n", d, invoiceService.revenueByDay(d));
                }
                case 2 -> {
                    System.out.print("Nhập tháng (1-12): ");
                    int m = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Nhập năm        : ");
                    int y = Integer.parseInt(scanner.nextLine().trim());
                    System.out.printf("Doanh thu tháng %d/%d: %,.0f VND%n", m, y, invoiceService.revenueByMonth(m, y));
                }
                case 3 -> {
                    System.out.print("Nhập năm: ");
                    int y = Integer.parseInt(scanner.nextLine().trim());
                    System.out.printf("Doanh thu năm %d: %,.0f VND%n", y, invoiceService.revenueByYear(y));
                }
                default -> System.out.println("Lựa chọn không hợp lệ.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Giá trị nhập không hợp lệ.");
        }
    }
}
