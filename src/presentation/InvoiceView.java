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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InvoiceView {
    private static final IInvoiceService invoiceService = new InvoiceServiceImpl();
    private static final IProductService productService = new ProductServiceImpl();
    private static final ICustomerService customerService = new CustomerServiceImpl();
    private static final InvoiceDetailDAOImpl invoiceDetailDAO = new InvoiceDetailDAOImpl();

    // =====================================================================
    // 3.3 QUẢN LÝ HÓA ĐƠN
    // =====================================================================

    public static void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========= QUẢN LÝ HÓA ĐƠN =========");
            System.out.println("1. Hiển thị danh sách hóa đơn");
            System.out.println("2. Thêm mới hóa đơn");
            System.out.println("3. Tìm kiếm hóa đơn");
            System.out.println("4. Quay lại menu chính");
            System.out.println("=====================================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> showAll(scanner);
                    case 2 -> addInvoice(scanner);
                    case 3 -> showSearchMenu(scanner);
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    // ------------------- Menu tìm kiếm con -------------------

    private static void showSearchMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- TÌM KIẾM HÓA ĐƠN ---");
            System.out.println("1. Tìm theo tên khách hàng");
            System.out.println("2. Tìm theo ngày/tháng/năm");
            System.out.println("3. Quay lại menu hóa đơn");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> findByCustomerName(scanner);
                    case 2 -> findByDate(scanner);
                    case 3 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    // =====================================================================
    // 3.4 THỐNG KÊ DOANH THU
    // =====================================================================

    public static void showRevenueMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========= THỐNG KÊ DOANH THU =========");
            System.out.println("1. Doanh thu theo ngày");
            System.out.println("2. Doanh thu theo tháng");
            System.out.println("3. Doanh thu theo năm");
            System.out.println("4. Quay lại menu chính");
            System.out.println("======================================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> revenueByDay(scanner);
                    case 2 -> revenueByMonth(scanner);
                    case 3 -> revenueByYear(scanner);
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    // =====================================================================
    // THÊM HÓA ĐƠN (hỗ trợ nhiều sản phẩm)
    // =====================================================================

    private static void addInvoice(Scanner scanner) {
        System.out.println("\n--- THÊM HÓA ĐƠN MỚI ---");
        try {
            // --- Chọn khách hàng ---
            List<Customer> customers = customerService.findAll();
            if (customers.isEmpty()) {
                System.out.println("Chưa có khách hàng. Vui lòng thêm khách hàng trước.");
                return;
            }
            System.out.println("== Danh sách khách hàng ==");
            customers.forEach(c -> System.out.printf("  [%d] %-25s %s%n",
                    c.getId(), c.getName(), c.getPhone()));
            System.out.print("Chọn ID khách hàng: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            Customer customer = customerService.findById(customerId);
            if (customer == null) {
                System.out.println("✗ Không tìm thấy khách hàng.");
                return;
            }

            // --- Chọn sản phẩm (vòng lặp để thêm nhiều loại) ---
            List<Product> products = productService.findAll();
            if (products.isEmpty()) {
                System.out.println("Chưa có sản phẩm.");
                return;
            }

            // Danh sách chi tiết sẽ lưu
            List<int[]> orderLines = new ArrayList<>(); // [productId, quantity]
            List<double[]> orderPrices = new ArrayList<>(); // [unitPrice, subtotal]
            double totalAmount = 0;

            while (true) {
                System.out.println("\n== Danh sách sản phẩm ==");
                products.forEach(p -> System.out.printf("  [%d] %-30s | %,10.0f VND | Tồn: %d%n",
                        p.getId(), p.getName(), p.getPrice(), p.getStock()));
                System.out.print("Chọn ID sản phẩm (0 để kết thúc nhập): ");
                int productId = Integer.parseInt(scanner.nextLine().trim());
                if (productId == 0)
                    break;

                Product product = productService.findById(productId);
                if (product == null) {
                    System.out.println("✗ Không tìm thấy sản phẩm, thử lại.");
                    continue;
                }
                if (product.getStock() <= 0) {
                    System.out.println("✗ Sản phẩm đã hết hàng!");
                    continue;
                }

                System.out.print("Số lượng       : ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("✗ Số lượng phải lớn hơn 0.");
                    continue;
                }
                if (quantity > product.getStock()) {
                    System.out.printf("✗ Tồn kho không đủ! Chỉ còn %d sản phẩm.%n", product.getStock());
                    continue;
                }

                double unitPrice = product.getPrice();
                double subtotal = unitPrice * quantity;
                totalAmount += subtotal;
                orderLines.add(new int[] { productId, quantity });
                orderPrices.add(new double[] { unitPrice, subtotal });
                System.out.printf("  → Đã thêm: %-30s | SL: %d | %,.0f VND%n",
                        product.getName(), quantity, subtotal);
            }

            if (orderLines.isEmpty()) {
                System.out.println("Chưa chọn sản phẩm nào. Đã hủy.");
                return;
            }

            // --- Xác nhận hóa đơn ---
            System.out.println("\n========== XÁC NHẬN HÓA ĐƠN ==========");
            System.out.printf("  Khách hàng : %s (%s)%n", customer.getName(), customer.getPhone());
            System.out.println("  Sản phẩm:");
            for (int i = 0; i < orderLines.size(); i++) {
                Product p = productService.findById(orderLines.get(i)[0]);
                String pName = p != null ? p.getName() : "ID#" + orderLines.get(i)[0];
                System.out.printf("    - %-30s  SL: %d  |  %,.0f VND%n",
                        pName, orderLines.get(i)[1], orderPrices.get(i)[1]);
            }
            System.out.printf("  Tổng tiền  : %,.0f VND%n", totalAmount);
            System.out.println("========================================");
            System.out.print("Xác nhận tạo hóa đơn? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                System.out.println("Đã hủy.");
                return;
            }

            // --- Lưu Invoice header ---
            Invoice invoice = new Invoice(0, customerId, LocalDateTime.now(), totalAmount);
            boolean saved = invoiceService.add(invoice);
            if (!saved) {
                System.out.println("✗ Lỗi khi tạo hóa đơn. Vui lòng thử lại.");
                return;
            }

            // Lấy invoiceId vừa tạo (lấy hóa đơn mới nhất của khách hàng này)
            List<Invoice> invoices = invoiceService.findByCustomerId(customerId);
            if (invoices.isEmpty()) {
                System.out.println("✗ Lỗi khi lấy hóa đơn vừa tạo.");
                return;
            }
            int newInvoiceId = invoices.get(0).getId();

            // --- Lưu InvoiceDetail và trừ tồn kho ---
            for (int i = 0; i < orderLines.size(); i++) {
                int pid = orderLines.get(i)[0];
                int qty = orderLines.get(i)[1];
                double price = orderPrices.get(i)[0];
                invoiceDetailDAO.add(new InvoiceDetail(0, newInvoiceId, pid, qty, price));
                Product p = productService.findById(pid);
                if (p != null) {
                    p.setStock(p.getStock() - qty);
                    productService.update(p);
                }
            }

            System.out.printf("✓ Tạo hóa đơn thành công! (Mã hóa đơn: #%d)%n", newInvoiceId);

        } catch (NumberFormatException e) {
            System.out.println("✗ Giá trị nhập không hợp lệ.");
        }
    }

    // =====================================================================
    // HIỂN THỊ DANH SÁCH / CHI TIẾT
    // =====================================================================

    private static void showAll(Scanner scanner) {
        List<Invoice> list = invoiceService.findAll();
        if (list.isEmpty()) {
            System.out.println("Không có hóa đơn nào.");
            return;
        }
        printHeader();
        list.forEach(iv -> printInvoiceRow(iv));
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
        Invoice inv = invoiceService.findById(invoiceId);
        if (inv == null) {
            System.out.println("✗ Không tìm thấy hóa đơn #" + invoiceId);
            return;
        }
        Customer customer = customerService.findById(inv.getCustomerId());
        String customerInfo = customer != null
                ? customer.getName() + " (" + customer.getPhone() + ")"
                : "ID#" + inv.getCustomerId();

        System.out.println("\n╔══════════════ CHI TIẾT HÓA ĐƠN #" + invoiceId + " ══════════════╗");
        System.out.printf("  Khách hàng : %s%n", customerInfo);
        System.out.printf("  Thời gian  : %s%n",
                inv.getCreatedAt() != null
                        ? inv.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                        : "N/A");
        System.out.println("  ─────────────────────────────────────────────────────────────");

        List<InvoiceDetail> details = invoiceDetailDAO.findByInvoiceId(invoiceId);
        if (details.isEmpty()) {
            System.out.println("  (Không có chi tiết)");
        } else {
            System.out.printf("  %-4s  %-35s  %6s  %16s  %16s%n",
                    "STT", "Tên sản phẩm", "SL", "Đơn giá (VND)", "Thành tiền (VND)");
            System.out.println("  ─────────────────────────────────────────────────────────────");
            int stt = 1;
            for (InvoiceDetail d : details) {
                Product p = productService.findById(d.getProductId());
                String pName = p != null ? p.getName() : "ID#" + d.getProductId();
                System.out.printf("  %-4d  %-35s  %6d  %,16.0f  %,16.0f%n",
                        stt++, pName, d.getQuantity(), d.getUnitPrice(), d.getSubtotal());
            }
        }
        System.out.println("  ─────────────────────────────────────────────────────────────");
        System.out.printf("  %52s  %,16.0f%n", "TỔNG TIỀN:", inv.getTotalAmount());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    private static void printHeader() {
        printDivider();
        System.out.printf("| %-6s | %-25s | %-19s | %20s |%n",
                "ID", "Khách hàng", "Thời gian tạo", "Tổng tiền (VND)");
        printDivider();
    }

    private static void printDivider() {
        System.out.println("+--------+---------------------------+---------------------+----------------------+");
    }

    private static void printInvoiceRow(Invoice iv) {
        Customer c = customerService.findById(iv.getCustomerId());
        String cName = c != null ? c.getName() : "ID#" + iv.getCustomerId();
        String timeStr = iv.getCreatedAt() != null
                ? iv.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                : "";
        System.out.printf("| %-6d | %-25s | %-19s | %,20.0f |%n",
                iv.getId(), cName, timeStr, iv.getTotalAmount());
    }

    private static void printResult(List<Invoice> list) {
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy hóa đơn phù hợp.");
        } else {
            printHeader();
            list.forEach(iv -> printInvoiceRow(iv));
            printDivider();
            System.out.println("  Tìm thấy: " + list.size() + " hóa đơn.");
        }
    }

    // =====================================================================
    // TÌM KIẾM
    // =====================================================================

    private static void findByCustomerName(Scanner scanner) {
        System.out.println("\n--- TÌM HÓA ĐƠN THEO TÊN KHÁCH HÀNG ---");
        System.out.print("Nhập tên khách hàng (hoặc một phần tên): ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("✗ Tên không được để trống.");
            return;
        }
        List<Customer> matched = customerService.findByName(keyword);
        if (matched.isEmpty()) {
            System.out.println("Không tìm thấy khách hàng nào có tên chứa \"" + keyword + "\".");
            return;
        }
        List<Invoice> result = new ArrayList<>();
        for (Customer c : matched) {
            result.addAll(invoiceService.findByCustomerId(c.getId()));
        }
        if (result.isEmpty()) {
            System.out.println("Khách hàng \"" + keyword + "\" chưa có hóa đơn nào.");
        } else {
            printResult(result);
        }
    }

    private static void findByDate(Scanner scanner) {
        System.out.println("\n--- TÌM HÓA ĐƠN THEO NGÀY ---");
        System.out.print("Nhập ngày (yyyy-MM-dd, ví dụ: 2025-03-10): ");
        String dateStr = scanner.nextLine().trim();
        // Validate định dạng ngày
        try {
            java.time.LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            System.out.println("✗ Định dạng ngày không hợp lệ. Vui lòng nhập theo dạng yyyy-MM-dd.");
            return;
        }
        printResult(invoiceService.findByDate(dateStr));
    }

    // =====================================================================
    // THỐNG KÊ DOANH THU
    // =====================================================================

    private static void revenueByDay(Scanner scanner) {
        System.out.print("Nhập ngày (yyyy-MM-dd, ví dụ: 2025-03-10): ");
        String dateStr = scanner.nextLine().trim();
        try {
            java.time.LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            System.out.println("✗ Định dạng ngày không hợp lệ.");
            return;
        }
        double revenue = invoiceService.revenueByDay(dateStr);
        System.out.println("────────────────────────────────────────");
        System.out.printf("  Doanh thu ngày %s : %,.0f VND%n",
                formatDate(dateStr), revenue);
        System.out.println("────────────────────────────────────────");
    }

    private static void revenueByMonth(Scanner scanner) {
        try {
            System.out.print("Nhập tháng (1-12) : ");
            int month = Integer.parseInt(scanner.nextLine().trim());
            if (month < 1 || month > 12) {
                System.out.println("✗ Tháng phải từ 1 đến 12.");
                return;
            }
            System.out.print("Nhập năm          : ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            double revenue = invoiceService.revenueByMonth(month, year);
            System.out.println("────────────────────────────────────────");
            System.out.printf("  Doanh thu tháng %02d/%d : %,.0f VND%n", month, year, revenue);
            System.out.println("────────────────────────────────────────");
        } catch (NumberFormatException e) {
            System.out.println("✗ Giá trị nhập không hợp lệ.");
        }
    }

    private static void revenueByYear(Scanner scanner) {
        try {
            System.out.print("Nhập năm : ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            double revenue = invoiceService.revenueByYear(year);
            System.out.println("────────────────────────────────────────");
            System.out.printf("  Doanh thu năm %d : %,.0f VND%n", year, revenue);
            System.out.println("────────────────────────────────────────");
        } catch (NumberFormatException e) {
            System.out.println("✗ Giá trị nhập không hợp lệ.");
        }
    }

    // =====================================================================
    // UTILITIES
    // =====================================================================

    /** Chuyển "2025-03-10" → "10/03/2025" cho hiển thị */
    private static String formatDate(String isoDate) {
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(isoDate);
            return d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return isoDate;
        }
    }
}
