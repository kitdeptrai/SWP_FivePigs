# Admin Flow 03 - Orders (Frontend → Backend → DAO)

> Mục tiêu của tài liệu này: vừa mô tả luồng xử lý đơn hàng cho admin, vừa giải thích ý nghĩa từng dòng để dễ đọc code thực tế.

## Frontend
File: `WEB-INF/views/admin/orders.jsp`

- Form lọc:
  - `GET /admin/orders`
    - Giải thích: Trang orders dùng phương thức `GET` để lọc dữ liệu, giúp URL giữ được trạng thái filter (có thể copy/share/reload).
  - Params: `keyword`, `fromDate`, `toDate`, `page`
    - `keyword`: từ khóa tìm kiếm (mã đơn, tên user, email... tùy logic JSP/Servlet).
    - `fromDate`: ngày bắt đầu lọc.
    - `toDate`: ngày kết thúc lọc.
    - `page`: số trang hiện tại để phân trang.

- Link xem chi tiết:
  - vẫn `GET /admin/orders` + `orderId=...`
    - Giải thích: Không tách endpoint mới, mà dùng cùng endpoint `/admin/orders` và thêm query `orderId` để biết đơn nào đang được chọn hiển thị chi tiết.

## Backend (`AdminOrdersServlet#doGet`)
- Đọc filter + `selectedOrderId`.
  - Giải thích: Servlet đọc `keyword/fromDate/toDate/page/orderId` từ request để biết người dùng đang lọc gì và có chọn đơn nào không.
- Gọi `adminService.getSuccessfulOrdersPage(...)` để lấy list + paging.
  - Giải thích: Đẩy logic xử lý filter/paging xuống Service, Servlet chỉ điều phối.
- Set attributes list/paging/filter.
  - Giải thích: Đưa dữ liệu vào request scope để JSP render danh sách + thanh phân trang + giữ lại giá trị filter trên form.
- Nếu có `selectedOrderId`:
  - gọi `adminService.getOrderDetails(selectedOrderId)`
    - Giải thích: Lấy danh sách item trong đúng đơn đang được chọn.
  - set `orderDetails`, `orderDetailsCount`, `orderDetailsTotal`
    - `orderDetails`: danh sách dòng sản phẩm trong đơn.
    - `orderDetailsCount`: tổng số dòng item (hoặc tổng sản phẩm tùy cách tính).
    - `orderDetailsTotal`: tổng tiền của phần chi tiết đang hiển thị.
- Render `orders.jsp`.
  - Giải thích: Kết thúc request bằng forward về JSP để hiển thị toàn bộ dữ liệu.

## Service (`AdminService`)
- `getSuccessfulOrdersPage(...)`:
  - parse/normalize filter ngày + keyword
    - Giải thích: Chuẩn hóa input (trim keyword, parse date an toàn, tránh null lỗi).
  - sửa thứ tự ngày nếu from > to
    - Giải thích: Nếu user nhập ngược ngày, service tự xử lý để query vẫn hợp lệ.
  - tính paging
    - Giải thích: Tính `limit`, `offset`, `currentPage`, `totalPages`.
  - gọi DAO count + list
    - Giải thích: Query 1 để đếm tổng bản ghi; Query 2 để lấy dữ liệu trang hiện tại.
- `getOrderDetails(orderId)`:
  - parse id
    - Giải thích: Chuyển `orderId` từ string sang number và validate cơ bản.
  - gọi DAO chi tiết order
    - Giải thích: Lấy danh sách item thuộc order này từ DB.

## DAO (`AdminDao`)
- `countSuccessfulOrders(keyword, fromDate, toDate)`
  - Giải thích: Trả về tổng số đơn thành công thỏa điều kiện filter, phục vụ phân trang.
- `listSuccessfulOrdersPaged(limit, offset, keyword, fromDate, toDate)`
  - Giải thích: Trả về danh sách đơn thành công theo trang hiện tại.
- `listOrderDetails(orderId)`
  - Giải thích: Trả về chi tiết item của 1 đơn cụ thể.

## Input / Output
- Input: keyword, fromDate, toDate, orderId
  - Giải thích: Đây là toàn bộ dữ liệu đầu vào từ query params của request GET.
- Output: danh sách đơn hàng + danh sách item trong đơn
  - Giải thích: JSP nhận cả phần master (orders) và detail (order details) để hiển thị theo kiểu master-detail.
- Side effect: không ghi DB (read-only flow)
  - Giải thích: Luồng này chỉ đọc dữ liệu báo cáo, không tạo/sửa/xóa dữ liệu trong database.

## Mapping nhanh từ URL đến tầng xử lý
- `GET /admin/orders?keyword=abc&fromDate=2025-01-01&toDate=2025-01-31&page=2`
  - Servlet đọc params
  - Service chuẩn hóa filter + tính trang
  - DAO count + list
  - Servlet set attribute và forward JSP
- `GET /admin/orders?orderId=123`
  - Servlet nhận `selectedOrderId=123`
  - Service gọi DAO lấy chi tiết đơn 123
  - JSP render phần detail tương ứng