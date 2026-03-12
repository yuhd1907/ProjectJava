package presentation;

import model.Admin;
import service.IAdminService;
import service.impl.AdminServiceImpl;

import java.util.Scanner;

public class AdminView {
    public static Admin userLogin = null;
    private static final IAdminService adminService = new AdminServiceImpl();

    public static void showMenuLogin(Scanner scanner) {
        System.out.println("========= ĐĂNG NHẬP QUẢN TRỊ =========");
        System.out.print("Tài khoản: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();
        System.out.println("=====================================");
        Admin admin = adminService.login(username, password);

        if (admin != null) {
            System.out.println("Đăng nhập thành công! Xin chào, " + admin.getUsername());
            userLogin = admin;
            showMainMenu(scanner);
        } else {
            System.out.println("Thông tin đăng nhập không hợp lệ. Vui lòng thử lại.");
            showMenuLogin(scanner);
        }
    }

    public static void showMainMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========= MENU CHÍNH =========");
            System.out.println("1. Quản lý sản phẩm điện thoại");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý hóa đơn");
            System.out.println("4. Thống kê doanh thu");
            System.out.println("5. Đăng xuất");
            System.out.println("==============================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        ProductView.showMenu(scanner);
                        break;
                    case 2:
                        CustomerView.showMenu(scanner);
                        break;
                    case 3:
                        InvoiceView.showMenu(scanner);
                        break;
                    case 4:
                        InvoiceView.showRevenueMenu(scanner);
                        break;
                    case 5:
                        System.out.println("Đã đăng xuất. Hẹn gặp lại, " + userLogin.getUsername() + "!");
                        userLogin = null;
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }
}
