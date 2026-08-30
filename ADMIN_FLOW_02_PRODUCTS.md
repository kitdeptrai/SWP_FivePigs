# Flow 02: Admin Products (Report sản phẩm)

## Frontend (JSP)
- File: `src/main/webapp/WEB-INF/views/admin/products.jsp`
- Filter:
  - `GET /admin/products`
  - Params: `keyword`, `status`, `page`
- Detail:
  - Link `GET /admin/products/detail?reportId=...`
- Action:
  - `POST /admin/products/approve` (reportId)
  - `POST /admin/products/reject` (reportId)

## Backend (Servlet + Service)
- `AdminProductsServlet#doGet()`:
  - gọi `adminService.getProductsPage(...)`
  - set list/paging/filter
  - render `products.jsp`

- `AdminProductDetailServlet#doGet()`:
  - lấy `reportId`
  - gọi `adminService.getProductReportDetail(reportId)`
  - null -> redirect lỗi; có data -> render detail

- `AdminProductActionServlet#doPost()`:
  - `/approve` -> `adminService.approveReport(reportId)`
  - `/reject` -> `adminService.rejectReport(reportId)`

## DAO
- File: `src/main/java/com/fivepigs/app/dao/AdminDao.java`
- Hàm chính:
  - `countProducts(keyword, status)`
  - `listProductsPaged(limit, offset, keyword, status)`
  - `getReportDetail(reportId)`
  - `updateReportStatus(reportId, fromStatus, toStatus)`
