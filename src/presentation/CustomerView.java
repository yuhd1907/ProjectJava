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
            System.out.println("5. Quay lại menu chính");
            System.out.println("=======================================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> showAll();
                    case 2 -> addCustomer(scanner);
                    case 3 -> updateCustomer(scanner);
                    case 4 -> deleteCustomer(scanner);
                    case 5 -> {
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

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
        System.out.printf("| %-4s | %-20s | %-13s | %-22s | %-20s |%n",
                "ID", "Họ và tên", "Điện thoại", "Email", "Địa chỉ");
        printDivider();
    }

    private static void printDivider() {
        System.out.println(
                "+------+----------------------+---------------+------------------------+----------------------+");
    }

    private static void addCustomer(Scanner scanner) {
        System.out.println("\n--- THÊM KHÁCH HÀNG MỚI ---");
        System.out.print("Họ và tên   : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Tên không được để trống.");
            return;
        }

        System.out.print("Điện thoại  : ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email       : ");
        String email = scanner.nextLine().trim();
        System.out.print("Địa chỉ     : ");
        String address = scanner.nextLine().trim();

        customerService.add(new Customer(0, name, phone, email, address));
        System.out.println("✓ Thêm khách hàng thành công!");
    }

    private static void updateCustomer(Scanner scanner) {
        showAll();
        System.out.println("\n--- CẬP NHẬT THÔNG TIN KHÁCH HÀNG ---");
        try {
            System.out.print("Nhập ID khách hàng cần cập nhật: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Customer c = customerService.findById(id);
            if (c == null) {
                System.out.println("Không tìm thấy khách hàng có ID = " + id);
                return;
            }

            System.out.println("Thông tin hiện tại:");
            printHeader();
            System.out.println(c);
            printDivider();
            System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)");

            System.out.print("Tên mới      : ");
            String name = scanner.nextLine().trim();
            System.out.print("SĐT mới      : ");
            String phone = scanner.nextLine().trim();
            System.out.print("Email mới    : ");
            String email = scanner.nextLine().trim();
            System.out.print("Địa chỉ mới  : ");
            String address = scanner.nextLine().trim();

            if (!name.isEmpty())
                c.setName(name);
            if (!phone.isEmpty())
                c.setPhone(phone);
            if (!email.isEmpty())
                c.setEmail(email);
            if (!address.isEmpty())
                c.setAddress(address);

            customerService.update(c);
            System.out.println("✓ Cập nhật khách hàng thành công!");
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        }
    }

    private static void deleteCustomer(Scanner scanner) {
        showAll();
        System.out.println("\n--- XÓA KHÁCH HÀNG THEO ID ---");
        try {
            System.out.print("Nhập ID khách hàng cần xóa: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Customer c = customerService.findById(id);
            if (c == null) {
                System.out.println("Không tìm thấy khách hàng có ID = " + id);
                return;
            }

            System.out.println("Khách hàng sẽ bị xóa:");
            printHeader();
            System.out.println(c);
            printDivider();
            System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                customerService.delete(id);
                System.out.println("✓ Xóa khách hàng thành công!");
            } else {
                System.out.println("Đã hủy thao tác xóa.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        }
    }
}
