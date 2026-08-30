# Admin Flow 04 - Vendors (Frontend → Backend → DAO)

## Frontend
File: `WEB-INF/views/admin/vendors.jsp`

- Filter vendors:
  - `GET /admin/vendors`
  - Params: `keyword`, `status`, `page`
- Disable vendor:
  - `POST /admin/vendors/disable` + `userId`
- Enable vendor:
  - `POST /admin/vendors/enable` + `userId`

## Backend

### `AdminVendorsServlet#doGet`
- Nhận filter.
- Gọi `adminService.getVendorsPage(...)`.
- Set `users`, `currentPage`, `totalPages`, `keyword`, `status`.
- Render `vendors.jsp`.

### `VendorActionServlet#doPost`
- Switch theo servletPath:
  - `/enable` → `handleStatus(..., "ACTIVE")`
  - `/disable` → `handleStatus(..., "INACTIVE")`
- `handleStatus` gọi `adminService.setUserStatus(userId, status)`.
- Redirect success/error.

## Service (`AdminService`)
- `getVendorsPage(...)`:
  - normalize filter
  - count + paging + list
- `setUserStatus(...)`:
  - validate id/status
  - gọi DAO update status user
  - broadcast notification cho admins

## DAO (`AdminDao`)
- `countVendors(keyword, status)`
- `listVendorsPaged(limit, offset, keyword, status)`
- `setUserStatus(userId, status)`

## Side effect
- Đổi trạng thái account vendor trong bảng `users`.
