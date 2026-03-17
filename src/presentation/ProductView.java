package presentation;

import model.Product;
import service.IProductService;
import service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Scanner;

public class ProductView {
    private static final IProductService productService = new ProductServiceImpl();

    public static void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n========== QUẢN LÝ SẢN PHẨM ==========");
            System.out.println("1. Hiển thị danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm mới");
            System.out.println("3. Cập nhật thông tin sản phẩm");
            System.out.println("4. Xóa sản phẩm theo ID");
            System.out.println("5. Tìm kiếm theo Brand");
            System.out.println("6. Tìm kiếm theo khoảng giá");
            System.out.println("7. Tìm kiếm theo tồn kho");
            System.out.println("8. Quay lại menu chính");
            System.out.println("=======================================");
            System.out.print("Nhập lựa chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        showAll();
                        break;
                    case 2:
                        addProduct(scanner);
                        break;
                    case 3:
                        updateProduct(scanner);
                        break;
                    case 4:
                        deleteProduct(scanner);
                        break;
                    case 5:
                        findByBrand(scanner);
                        break;
                    case 6:
                        findByPriceRange(scanner);
                        break;
                    case 7:
                        findByStock(scanner);
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập lại.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    // ==================== HIỂN THỊ ====================

    private static void showAll() {
        List<Product> list = productService.findAll();
        if (list.isEmpty()) {
            System.out.println("Không có sản phẩm nào trong danh sách.");
            return;
        }
        printTableHeader();
        list.forEach(System.out::println);
        printDivider();
        System.out.println("  Tổng cộng: " + list.size() + " sản phẩm.");
    }

    private static void printResult(List<Product> list, String context) {
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào " + context + ".");
        } else {
            printTableHeader();
            list.forEach(System.out::println);
            printDivider();
            System.out.println("  Tìm thấy " + list.size() + " sản phẩm.");
        }
    }

    private static void printTableHeader() {
        printDivider();
        System.out.printf("| %-5s | %-35s | %-12s | %12s | %-8s |\n",
                "ID", "Tên sản phẩm", "Hãng", "Giá (VND)", "Tồn kho");
        printDivider();
    }

    private static void printDivider() {
        System.out.printf("+%s+%s+%s+%s+%s+\n", "-".repeat(7), "-".repeat(37), "-".repeat(14), "-".repeat(14), "-".repeat(10));
    }

    // ==================== THÊM ====================

    private static void addProduct(Scanner scanner) {
        System.out.println("\n--- THÊM SẢN PHẨM MỚI ---");
        try {
            System.out.print("Tên sản phẩm : ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Tên không được để trống.");
                return;
            }

            System.out.print("Hãng (Brand)  : ");
            String brand = scanner.nextLine().trim();
            if (brand.isEmpty()) {
                System.out.println("Hãng không được để trống.");
                return;
            }

            System.out.print("Giá (VND)     : ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            if (price <= 0) {
                System.out.println("Giá phải lớn hơn 0.");
                return;
            }
            if (price > 99999999.99) {
                System.out.println("Giá vượt quá giới hạn (tối đa 99,999,999.99 VND).");
                return;
            }

            System.out.print("Tồn kho       : ");
            int stock = Integer.parseInt(scanner.nextLine().trim());
            if (stock < 0) {
                System.out.println("Tồn kho không được âm.");
                return;
            }

            productService.add(new Product(0, name, brand, price, stock));
            System.out.println("Thêm sản phẩm thành công!");
        } catch (NumberFormatException e) {
            System.out.println("Giá hoặc tồn kho không hợp lệ.");
        }
    }

    // ==================== CẬP NHẬT ====================

    private static void updateProduct(Scanner scanner) {
        showAll();
        System.out.println("\n--- CẬP NHẬT THÔNG TIN SẢN PHẨM ---");
        try {
            System.out.print("Nhập ID sản phẩm cần cập nhật: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Product p = productService.findById(id);
            if (p == null) {
                System.out.println("ID sản phẩm không tồn tại.");
                updateProduct(scanner);
            }

            System.out.println("Thông tin hiện tại:");
//            printTableHeader();
//            System.out.println(p);
//            printDivider();
            System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)");

            System.out.print("Tên mới       : ");
            String name = scanner.nextLine().trim();
            System.out.print("Hãng mới      : ");
            String brand = scanner.nextLine().trim();
            System.out.print("Giá mới (VND) : ");
            String priceStr = scanner.nextLine().trim();
            System.out.print("Tồn kho mới   : ");
            String stockStr = scanner.nextLine().trim();

            if (!name.isEmpty())
                p.setName(name);
            if (!brand.isEmpty())
                p.setBrand(brand);
            if (!priceStr.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    System.out.println("Giá không hợp lệ.");
                    return;
                }
                if (price > 99999999.99) {
                    System.out.println("Giá vượt quá giới hạn (tối đa 99,999,999.99 VND).");
                    return;
                }
                p.setPrice(price);
            }
            if (!stockStr.isEmpty()) {
                int stock = Integer.parseInt(stockStr);
                if (stock >= 0)
                    p.setStock(stock);
                else {
                    System.out.println("Tồn kho không hợp lệ.");
                    return;
                }
            }

            productService.update(p);
            System.out.println("Cập nhật sản phẩm thành công!");
        } catch (NumberFormatException e) {
            System.out.println("Giá trị nhập không hợp lệ.");
        }
    }

    // ==================== XÓA ====================

    private static void deleteProduct(Scanner scanner) {
        showAll();
        System.out.println("\n--- XÓA SẢN PHẨM THEO ID ---");
        try {
            System.out.print("Nhập ID sản phẩm cần xóa: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Product p = productService.findById(id);
            if (p == null) {
                System.out.println("ID sản phẩm không tồn tại.");
                return;
            }

            System.out.println("Sản phẩm sẽ bị xóa:");
            printTableHeader();
            System.out.println(p);
            printDivider();
            System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                productService.delete(id);
                System.out.println("Xóa sản phẩm thành công!");
            } else {
                System.out.println("Đã hủy thao tác xóa.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        }
    }

    // ==================== TÌM KIẾM ====================

    private static void findByBrand(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM THEO BRAND ---");
        System.out.print("Nhập tên hãng (Brand): ");
        String brand = scanner.nextLine().trim();
        if (brand.isEmpty()) {
            System.out.println("Tên hãng không được để trống.");
            return;
        }
        printResult(productService.findByBrand(brand), "thuộc hãng \"" + brand + "\"");
    }

    private static void findByPriceRange(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM THEO KHOẢNG GIÁ ---");
        try {
            System.out.print("Giá tối thiểu (VND): ");
            double min = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Giá tối đa   (VND): ");
            double max = Double.parseDouble(scanner.nextLine().trim());
            if (min > max) {
                System.out.println("Giá tối thiểu không được lớn hơn giá tối đa.");
                return;
            }
            printResult(productService.findByPriceRange(min, max),
                    String.format("trong khoảng %,.0f - %,.0f VND", min, max));
        } catch (NumberFormatException e) {
            System.out.println("Giá trị nhập không hợp lệ.");
        }
    }

    private static void findByStock(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM THEO TỒN KHO ---");
        try {
            System.out.print("Tồn kho tối thiểu: ");
            int minStock = Integer.parseInt(scanner.nextLine().trim());
            if (minStock < 0) {
                System.out.println("Tồn kho không được âm.");
                return;
            }
            printResult(productService.findByMinStock(minStock), "có tồn kho >= " + minStock);
        } catch (NumberFormatException e) {
            System.out.println("Giá trị nhập không hợp lệ.");
        }
    }
}
