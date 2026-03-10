import presentation.AdminView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    ========= HỆ THỐNG QUẢN LÝ CỬA HÀNG =========
                    1. Đăng nhập Admin
                    2. Thoát
                    =============================================
                    """);
            System.out.print("Nhập lựa chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    AdminView.showMenuLogin(scanner);
                    break;
                    case 2:
                        System.out.println("Thoát chương trình.");
                        scanner.close();
                        return;
                        default:
                            System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại");
            }

        }
    }
}
