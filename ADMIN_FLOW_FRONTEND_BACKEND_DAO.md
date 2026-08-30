# Admin Flow: Frontend → Backend → DAO (FivePigs)

Tài liệu này mô tả các luồng chính của khu vực Admin theo đúng thứ tự:

1. **Frontend (JSP / form / action / link)**
2. **Backend (Servlet / Service)**
3. **DAO (SQL query chạy xuống DB)**

## A. Tổng quan routing Admin

Các trang chính của admin:
- `/admin/dashboard`
- `/admin/products`
- `/admin/products/detail`
- `/admin/orders`
- `/admin/vendors`
- `/admin/employees`
- `/admin/reports`
- `/admin_notifications`

Các action POST chính:
- `/admin/products/approve`, `/admin/products/reject`
- `/admin/vendors/enable`, `/admin/vendors/disable`
- `/admin/employees/create`, `/admin/employees/update`, `/admin/employees/enable`, `/admin/employees/disable`
- `/admin/reports` (action `markRead`)
- `/admin/dashboard` (update commission)
- `/admin_notifications` (toggle/delete/markAllRead/deleteAll)

## B. Dashboard Admin

### Frontend
File: `WEB-INF/views/admin/dashboard.jsp`
- Form cập nhật commission: `POST /admin/dashboard`
- Input chính: `commissionPercent`

### Backend
File: `AdminDashboardServlet`
- `doGet()`: lấy metric dashboard từ `AdminDao`, set attributes, forward `dashboard.jsp`
- `doPost()`: validate `commissionPercent` (bắt buộc, số, 0..20), gọi `setCommissionPercent`, rồi redirect

### DAO
File: `AdminDao`
- `getTotalRevenue()`
- `getTotalProducts()`
- `getTotalUsers()`
- `getNewUsersToday()`
- `getTotalDownloads()`
- `getUnreadOrPendingReports()`
- `getRevenueByMonth()`
- `getTop5AppsBestSeller()`
- `getCommissionPercent()` / `setCommissionPercent(...)`

## C. Products (Report sản phẩm)

### Frontend
File: `admin/products.jsp`
- Filter: `GET /admin/products` với `keyword`, `status`
- Detail: `/admin/products/detail?reportId=...`
- Approve/Reject: `POST /admin/products/approve|reject` + `reportId`

### Backend
- `AdminProductsServlet#doGet()`: gọi `adminService.getProductsPage(...)`, set list + paging
- `AdminProductDetailServlet#doGet()`: lấy detail theo `reportId`
- `AdminProductActionServlet#doPost()`: approve/reject theo `servletPath`

### Service
File: `AdminService`
- `getProductsPage(...)`
- `approveReport(reportId)`
- `rejectReport(reportId)`
- `getProductReportDetail(reportId)`

### DAO
File: `AdminDao`
- `countProducts(...)`
- `listProductsPaged(...)`
- `getReportDetail(reportId)`
- `updateReportStatus(reportId, fromStatus, toStatus)`

## D. Orders

### Frontend
File: `admin/orders.jsp`
- Filter: `GET /admin/orders` với `keyword`, `fromDate`, `toDate`
- View detail: thêm `orderId` vào query

### Backend
File: `AdminOrdersServlet`
- gọi `getSuccessfulOrdersPage(...)`
- nếu có `orderId`, gọi `getOrderDetails(orderId)`

### Service
- `getSuccessfulOrdersPage(...)`
- `getOrderDetails(orderId)`

### DAO
- `countSuccessfulOrders(...)`
- `listSuccessfulOrdersPaged(...)`
- `listOrderDetails(orderId)`

## E. Vendors

### Frontend
File: `admin/vendors.jsp`
- Filter: `GET /admin/vendors` (`keyword`, `status`)
- Action: `POST /admin/vendors/enable|disable` (`userId`)

### Backend
- `AdminVendorsServlet#doGet()`
- `VendorActionServlet#doPost()`

### Service
- `getVendorsPage(...)`
- `setUserStatus(userId, ACTIVE/INACTIVE)`

### DAO
- `countVendors(...)`
- `listVendorsPaged(...)`
- `setUserStatus(...)`

## F. Employees

### Frontend
File: `admin/employees.jsp`
- Filter: `GET /admin/employees` (`keyword`, `role`, `status`)
- Create: `POST /admin/employees/create`
- Update: `POST /admin/employees/update`
- Enable/Disable: `POST /admin/employees/enable|disable`

### Backend
- `AdminEmployeesServlet#doGet()`
- `EmployeeCreateServlet#doPost()`
- `EmployeeActionServlet#doPost()`

### Service
- `getEmployeesPage(...)`
- `updateEmployee(...)`
- `setUserStatus(...)`

### DAO
- `emailExists(email)`
- `createEmployee(...)` (đã hash password trước khi insert)
- `countEmployees(...)`
- `listEmployeesPaged(...)`
- `updateUser(...)`
- `setUserStatus(...)`

## G. Reports (user_feedback)

### Frontend
File: `admin/reports.jsp`
- Filter: `GET /admin/reports` (`keyword`, `status`)
- Mark read: `POST /admin/reports` với `action=markRead`, `feedbackId`

### Backend
File: `AdminReportsServlet`
- `doGet()`: list + paging + detail theo `feedbackId`
- `doPost()`: xử lý `markRead`

### Service
- `getUserFeedbackPage(...)`
- `getFeedbackDetail(feedbackId)`
- `markFeedbackAsRead(feedbackId)`

### DAO
- `countUserFeedback(...)`
- `listUserFeedbackPaged(...)`
- `getUserFeedbackDetail(...)`
- `markUserFeedbackAsRead(...)`

## H. Notifications Admin

### Frontend
File: `admin/notifications.jsp`
- List/filter: `GET /admin_notifications` (`read`, `type`, `keyword`)
- Actions: `POST /admin_notifications` (`toggle`, `delete`, `markAllRead`, `deleteAll`)

### Backend
File: `AdminNotificationsServlet`
- `doGet()`: check admin, filter list, thống kê count
- `doPost()`: xử lý action, redirect lại list

### DAO
File: `NotificationDao`
- `filterByUser(...)`
- `countByUser(...)`, `countUnreadByUser(...)`, `countHighPriorityByUser(...)`
- `toggleRead(...)`, `delete(...)`, `markAllRead(...)`, `deleteAll(...)`

## I. Điều hướng Sidebar

File: `admin/sidebar.jsp`
- Dashboard → `/admin/dashboard`
- Notifications → `/admin_notifications`
- Employees → `/admin/employees`
- Vendors → `/admin/vendors`
- Products → `/admin/products`
- Orders → `/admin/orders`
- Reports → `/admin/reports`

## J. Phân quyền admin

- Nhóm servlet kế thừa `DashboardServlet` đều override `isAuthorized()` chỉ cho admin.
- `AdminNotificationsServlet` có check role trực tiếp.
- Bạn cũng đã yêu cầu logic redirect non-admin về dashboard theo role tương ứng.

Nếu bạn muốn, mình có thể viết tiếp bản V2 theo dạng **click-by-click sequence** (ví dụ bấm nút Approve ở UI thì chạy qua method nào, SQL nào, và dữ liệu đổi ra sao).