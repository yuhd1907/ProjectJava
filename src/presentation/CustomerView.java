package presentation;

import model.Customer;
import service.ICustomerService;
import service.impl.CustomerServiceImpl;

import java.util.List;
import java.util.Scanner;

public class CustomerView {
    private static final ICustomerService customerService = new CustomerServiceImpl();

    public static void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========= QUẢN LÝ KHÁCH HÀNG =========");
            System.out.println("1. Hiển thị danh sách khách hàng");
            System.out.println("2. Thêm khách hàng mới");
            System.out.println("3. Cập nhật thông tin khách hàng");
            System.out.println("4. Xóa khách hàng theo ID");
            System.out.println("5. Tìm kiếm khách hàng theo tên");
            System.out.println("6. Quay lại menu chính");
            System.out.println("=======================================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> showAll();
                    case 2 -> addCustomer(scanner);
                    case 3 -> updateCustomer(scanner);
                    case 4 -> deleteCustomer(scanner);
                    case 5 -> searchCustomer(scanner);
                    case 6 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    // ===================== HIỂN THỊ =====================

    private static void showAll() {
        List<Customer> list = customerService.findAll();
        if (list.isEmpty()) {
            System.out.println("Không có khách hàng nào.");
            return;
        }
        printHeader();
        list.forEach(System.out::println);
        printDivider();
        System.out.println("  Tổng cộng: " + list.size() + " khách hàng.");
    }

    private static void printHeader() {
        printDivider();
        System.out.printf("| %-4s | %-20s | %-13s | %-30s | %-20s |%n",
                "ID", "Họ và tên", "Điện thoại", "Email", "Địa chỉ");
        printDivider();
    }

    private static void printDivider() {
        System.out.println(
                "+------+----------------------+---------------+--------------------------------+----------------------+");
    }

    // ===================== THÊM =====================

    private static void addCustomer(Scanner scanner) {
        System.out.println("\n--- THÊM KHÁCH HÀNG MỚI ---");

        System.out.print("Họ và tên   : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("✗ Tên không được để trống.");
            return;
        }

        String phone = inputPhone(scanner, "Điện thoại  : ", -1);
        if (phone == null)
            return;

        String email = inputEmail(scanner, "Email       : ");
        if (email == null)
            return;

        System.out.print("Địa chỉ     : ");
        String address = scanner.nextLine().trim();

        boolean ok = customerService.add(new Customer(0, name, phone, email, address));
        if (ok) {
            System.out.println("✓ Thêm khách hàng thành công!");
        } else {
            System.out.println("✗ Thêm khách hàng thất bại. Vui lòng thử lại.");
        }
    }

    // ===================== CẬP NHẬT =====================

    private static void updateCustomer(Scanner scanner) {
        showAll();
        System.out.println("\n--- CẬP NHẬT THÔNG TIN KHÁCH HÀNG ---");
        try {
            System.out.print("Nhập ID khách hàng cần cập nhật: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Customer c = customerService.findById(id);
            if (c == null) {
                System.out.println("✗ Không tìm thấy khách hàng có ID = " + id);
                return;
            }

            System.out.println("Thông tin hiện tại:");
            printHeader();
            System.out.println(c);
            printDivider();
            System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)");

            System.out.print("Tên mới      : ");
            String name = scanner.nextLine().trim();

            // Nhập SĐT mới (bỏ qua nếu Enter)
            System.out.print("SĐT mới      : ");
            String phoneInput = scanner.nextLine().trim();
            String phone = c.getPhone(); // giữ cũ mặc định
            if (!phoneInput.isEmpty()) {
                if (!CustomerServiceImpl.isValidPhone(phoneInput)) {
                    System.out.println("✗ Số điện thoại phải có đúng 10 chữ số và bắt đầu bằng 0.");
                    return;
                }
                if (customerService.existsByPhone(phoneInput, id)) {
                    System.out.println("✗ Số điện thoại " + phoneInput + " đã được sử dụng bởi khách hàng khác.");
                    return;
                }
                phone = phoneInput;
            }

            // Nhập email mới (bỏ qua nếu Enter)
            System.out.print("Email mới    : ");
            String emailInput = scanner.nextLine().trim();
            String email = c.getEmail(); // giữ cũ mặc định
            if (!emailInput.isEmpty()) {
                if (!CustomerServiceImpl.isValidEmail(emailInput)) {
                    System.out.println("✗ Email phải có định dạng ...@gmail.com");
                    return;
                }
                email = emailInput;
            }

            System.out.print("Địa chỉ mới  : ");
            String address = scanner.nextLine().trim();

            if (!name.isEmpty())
                c.setName(name);
            c.setPhone(phone);
            c.setEmail(email);
            if (!address.isEmpty())
                c.setAddress(address);

            boolean ok = customerService.update(c);
            if (ok) {
                System.out.println("✓ Cập nhật khách hàng thành công!");
            } else {
                System.out.println("✗ Cập nhật thất bại. Vui lòng thử lại.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ ID không hợp lệ.");
        }
    }

    // ===================== XÓA =====================

    private static void deleteCustomer(Scanner scanner) {
        showAll();
        System.out.println("\n--- XÓA KHÁCH HÀNG THEO ID ---");
        try {
            System.out.print("Nhập ID khách hàng cần xóa: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Customer c = customerService.findById(id);
            if (c == null) {
                System.out.println("✗ Không tìm thấy khách hàng có ID = " + id);
                return;
            }

            System.out.println("Khách hàng sẽ bị xóa:");
            printHeader();
            System.out.println(c);
            printDivider();
            System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                boolean ok = customerService.delete(id);
                if (ok) {
                    System.out.println("✓ Xóa khách hàng thành công!");
                } else {
                    System.out.println("✗ Xóa thất bại. Vui lòng thử lại.");
                }
            } else {
                System.out.println("Đã hủy thao tác xóa.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ ID không hợp lệ.");
        }
    }

    // ===================== TÌM KIẾM =====================

    private static void searchCustomer(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM KHÁCH HÀNG THEO TÊN ---");
        System.out.print("Nhập tên cần tìm: ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("✗ Từ khóa không được để trống.");
            return;
        }
        List<Customer> results = customerService.findByName(keyword);
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy khách hàng nào có tên chứa \"" + keyword + "\".");
        } else {
            printHeader();
            results.forEach(System.out::println);
            printDivider();
            System.out.println("  Tìm thấy: " + results.size() + " khách hàng.");
        }
    }

    // ===================== HELPERS NHẬP LIỆU =====================

    /**
     * Nhập số điện thoại có validation: đúng 10 số, bắt đầu 0, không trùng (trừ
     * excludeId).
     * excludeId = -1 nghĩa là kiểm tra trùng tuyệt đối (khi thêm mới).
     * Trả null nếu nhập sai.
     */
    private static String inputPhone(Scanner scanner, String prompt, int excludeId) {
        System.out.print(prompt);
        String phone = scanner.nextLine().trim();
        if (!CustomerServiceImpl.isValidPhone(phone)) {
            System.out.println("✗ Số điện thoại phải có đúng 10 chữ số và bắt đầu bằng 0.");
            return null;
        }
        if (customerService.existsByPhone(phone, excludeId)) {
            System.out.println("✗ Số điện thoại " + phone + " đã được sử dụng bởi khách hàng khác.");
            return null;
        }
        return phone;
    }

    /**
     * Nhập email có validation: phải kết thúc bằng @gmail.com.
     * Trả null nếu nhập sai.
     */
    private static String inputEmail(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String email = scanner.nextLine().trim();
        if (!CustomerServiceImpl.isValidEmail(email)) {
            System.out.println("✗ Email phải có định dạng ...@gmail.com (ví dụ: ten@gmail.com).");
            return null;
        }
        return email;
    }
}
