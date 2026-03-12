# MÔ TẢ LUỒNG HOẠT ĐỘNG CỦA HỆ THỐNG QUẢN LÝ CỬA HÀNG ĐIỆN THOẠI

## 1. KHỞI ĐỘNG VÀ ĐĂNG NHẬP
Khi chạy chương trình chính (Main.java), hệ thống thực hiện các bước:
- Hiển thị màn hình chào mừng và Menu Chính của hệ thống.
- Người dùng chọn chức năng Đăng nhập. Hệ thống yêu cầu nhập Tên đăng nhập (username) và Mật khẩu (password). Mật khẩu được hiển thị dưới dạng các dấu sao để bảo mật.
- Hệ thống kiểm tra thông tin với cơ sở dữ liệu thông qua AdminDAO. Nếu sai, thông báo lỗi và yêu cầu nhập lại. Nếu đúng, hiển thị thông báo chào mừng và chuyển vào Màn hình Quản trị.

## 2. MÀN HÌNH QUẢN TRỊ (MENU CHÍNH)
Sau khi đăng nhập thành công, AdminView.java sẽ nắm quyền điều khiển. Hệ thống cung cấp các nhóm chức năng chính để Quản trị viên lựa chọn:
- Quản lý sản phẩm điện thoại
- Quản lý khách hàng
- Quản lý hóa đơn
- Thống kê doanh thu
- Đăng xuất (Quay lại màn hình khởi động/Thoát)
Mỗi phần quản lý được tách vào từng View riêng biệt (ProductView, CustomerView, InvoiceView).

## 3. LUỒNG QUẢN LÝ SẢN PHẨM (ProductView)
Đây là chức năng quản lý kho hàng của cửa hàng.
- Hiển thị danh sách: Lấy toàn bộ dữ liệu từ bảng product trong Database, in ra màn hình với đầy đủ tên, giá, tồn kho, nhãn hàng.
- Thêm mới sản phẩm: Yêu cầu nhập tên, nhãn hàng, mức giá, và số lượng tồn kho. Hệ thống kiểm tra kiểu dữ liệu hợp lệ và kiểm tra giới hạn giá (không vượt quá giới hạn kiểu Decimal 10,2 - 99,999,999.99 VND). Xuống tầng Service và lưu xuống Database thông qua ProductDAO.
- Cập nhật sản phẩm: Yêu cầu nhập ID cần thay đổi. Nếu tìm thấy, hệ thống cho phép nhập thông tin mới, hoặc bỏ trống nếu muốn giữ nguyên thông tin cũ. Các kiểm tra logic tương tự như thêm mới.
- Xóa sản phẩm: Nhập ID, hệ thống kiểm tra sự tồn tại và cần phải xác nhận (Y/N) trước khi xóa. Kiểm tra tính toàn vẹn dữ liệu nếu sản phẩm đã nằm trong hóa đơn.
- Tìm kiếm sản phẩm: Cho phép nhập từ khóa để tìm theo nhãn hàng, nhập khoảng giá (Từ - Đến), hoặc tìm những sản phẩm có số lượng tồn kho dưới một mức nhất định, dễ dàng châm hàng.

## 4. LUỒNG QUẢN LÝ KHÁCH HÀNG (CustomerView)
Chức năng này theo dõi thông tin khách đến mua hàng hoặc đăng ký thành viên.
- Thêm mới khách hàng: Yêu cầu nhập Họ tên (bắt buộc), số điện thoại (bắt buộc 10 số bắt đầu bằng số 0 và phải duy nhất trong hệ thống), email (bắt buộc có hậu tố ...@gmail.com và duy nhất), cùng với địa chỉ. 
- Hiển thị/Cập nhật/Xóa: Tương tự như sản phẩm, việc cập nhật luôn yêu cầu xác minh tính toàn vẹn (ví dụ: không cho phép nhập trùng số điện thoại với khách hàng khác). Việc xóa khách hàng sẽ hỏi xác nhận để phòng sai sót.
- Tìm kiếm: Nhập tên hoặc một phần tên. Hệ thống lấy kết quả từ DB thông qua CustomerDAO có chứa tên trùng khớp và in ra màn hình.

## 5. LUỒNG QUẢN LÝ HÓA ĐƠN (InvoiceView)
Đây là phần nghiệp vụ cốt lõi, tính toán và ràng buộc chặt chẽ nhất.
- Tạo hóa đơn mới: 
    + Bước 1: Hiển thị danh sách khách hàng cho admin chọn ID. Nếu nhập không tồn tại, yêu cầu chọn lại.
    + Bước 2: Hiển thị danh sách điện thoại. Người dùng nhập ID sản phẩm. 
    + Bước 3: Hệ thống xác thực ID sản phẩm có tồn tại hay không, và kiểm tra mức tồn kho còn đủ dùng hay không. Nếu không đủ kho hoặc hết hàng sẽ báo lỗi ngay.
    + Bước 4: Nhập số lượng mua. Hệ thống thực hiện kiểm tra mức giá thành tiền của từng món và mức tổng tiền của toàn bộ hóa đơn xem có quá giới hạn quy định của Database (99 triệu) không. 
    + Bước 5: Bổ sung thành công từng sản phẩm. Người dùng nhấn ID = 0 để kết thúc quá trình thêm món.
    + Bước 6: Lưu hóa đơn (Invoice) vào Database, lấy ID hóa đơn vừa tạo (Auto Increment), phân bổ ID đó để lưu các chi tiết hóa đơn để hình thành quan hệ bảng (InvoiceDetails). Và âm thầm cập nhật giảm trừ tồn kho (Product) cho các mặt hàng trong hóa đơn dưới DB.
- Hiển thị hóa đơn: Liệt kê hàng loạt hóa đơn gồm Mã, tên khách hàng, Ngày lập, Tổng tiền.
- Chi tiết hóa đơn: Nhập ID hóa đơn, hệ thống truy vấn bảng khách hàng để lấy tên, lấy ngày lập, vào bảng chi tiết (Invoice_Details) bóc tách từng món hàng (tên, số lượng, đơn giá). Tính toán thành tiền cho từng món và in biên lai ra console.
- Tra cứu: Tìm các hóa đơn thuộc về 1 khách hàng nào đó (thông qua tên) hoặc theo 1 ngày lập cụ thể.

## 6. THỐNG KÊ DOANH THU (Revenue)
Nhóm này nằm bên trong Menu Hóa đơn và giúp theo dõi hiệu quả kinh doanh.
- Doanh thu theo Ngày: Nhập ngày chuẩn (yyyy-MM-dd), hệ thống cộng dồn cột total_amount từ cơ sở dữ liệu thuộc về ngày đó rồi trả về hiển thị.
- Doanh thu theo Tháng: Nhập tháng và năm. Sử dụng câu lệnh truy vấn của DB để lấy ngày doanh thu khớp.
- Doanh thu theo Năm: Tương tự, bộ lọc chỉ lấy theo năm. Hệ thống in ra tổng giá trị tiền lưu thông.

## 7. ĐĂNG XUẤT VÀ KẾT THÚC
- Từ màn hình giao diện Admin, người quản trị chọn "Đăng xuất".
- Phiên làm việc tại Session Java bị hủy (Biến static UserLogin = null).
- Quay về màn hình Đăng nhập từ ban đầu cho các người dùng khác đăng nhập vào, hoặc chọn "Thoát" khỏi chương trình chính.

Hệ thống này tuân thủ nghiêm ngặt khuôn mẫu quản lý Model-View-Controller bản console. Các class được hoạt động tương đối độc lập từng nhóm qua lớp Service kiểm soát. Không có sự phụ thuộc phức tạp, các validation hoạt động xuyên suốt tránh nhập dữ liệu lỗi gây lỗi trong cơ sở dữ liệu bên dưới PostgreSQL.