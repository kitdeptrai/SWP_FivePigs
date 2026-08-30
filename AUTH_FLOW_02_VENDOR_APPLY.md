# AUTH FLOW 02: Apply Lên Vendor (Giải Thích Chi Tiết Theo Dòng)

Tài liệu này mô tả chi tiết luồng `Apply Become Vendor` từ UI đến callback thanh toán VNPay và cập nhật role `Vendor` trong DB.

## 1) Phạm vi file được giải thích

- `src/main/webapp/WEB-INF/views/customer/sidebar.jsp`
- `src/main/webapp/WEB-INF/views/customer/vendor-apply.jsp`
- `src/main/java/com/fivepigs/app/web/customer/CustomerAuthorizationFilter.java`
- `src/main/java/com/fivepigs/app/web/customer/VendorApplyServlet.java`
- `src/main/java/com/fivepigs/app/web/payment/VNPayPaymentServlet.java`
- `src/main/java/com/fivepigs/app/web/payment/VNPayReturnServlet.java`
- `src/main/java/com/fivepigs/app/dao/UserDao.java`
- `src/main/java/com/fivepigs/app/dao/NotificationDao.java`
- `src/main/java/com/fivepigs/app/config/VNPayConfig.java`
- `src/main/java/com/fivepigs/app/util/VNPayUtil.java`

## 2) Sequence tổng quan (thực tế chạy)

1. User bấm `Apply Become Vendor` ở sidebar.
2. `GET /vendor-apply` -> hiển thị trang policy + phí.
3. User tick đồng ý policy và submit form `POST /vendor-apply`.
4. Server lưu trạng thái pending vào session rồi redirect `/create-payment`.
5. `VNPayPaymentServlet` tạo URL thanh toán VNPay + chữ ký, redirect sang cổng VNPay.
6. VNPay callback về `/vnpay-return`.
7. Nếu thanh toán hợp lệ trong nhánh vendor apply -> cập nhật role user thành `Vendor`, set lại session, tạo notification, redirect `/vendor/dashboard`.

---

## 3) Frontend entry point

### 3.1 Link điều hướng trong sidebar

File: `sidebar.jsp`
- Dòng 13-15: đọc `roleName` từ session và xác định `isVendor`.
- Dòng 53-56: nếu đã là vendor thì hiện link `Vendor Dashboard`.
- Dòng 58-60: nếu chưa là vendor thì hiện link `Apply Become Vendor` trỏ đến `/vendor-apply`.

Ý nghĩa: UI tự đổi menu theo role hiện tại để tránh user vendor apply lặp.

### 3.2 Form apply vendor

File: `vendor-apply.jsp`
- Dòng 99: form `POST` vào `/vendor-apply`.
- Dòng 100: hidden input `agreePolicy` mặc định `false`.
- Dòng 102-106: nút submit bị disable mặc định.
- Dòng 115-129: JS bắt sự kiện checkbox:
  - checked -> enable nút + set hidden = `true`
  - unchecked -> disable nút + set hidden = `false`
- Dòng 92-96: nếu query `error=must_agree` thì hiển thị lỗi bắt buộc đồng ý policy.

Ý nghĩa: frontend chặn trước, backend vẫn validate lại để đảm bảo an toàn.

---

## 4) Filter bảo vệ route `/vendor-apply`

File: `CustomerAuthorizationFilter.java`

### 4.1 Route có áp filter
- Dòng 18-34: `/vendor-apply` nằm trong danh sách URL bắt buộc qua filter.

### 4.2 Logic filter
- Dòng 47-48: lấy session user.
- Dòng 50-62: nếu chưa login:
  - lấy URL hiện tại,
  - encode vào query `redirect=...`,
  - redirect sang `/login`.
- Dòng 64-68: nếu role không thuộc nhóm được phép truy cập customer pages thì đẩy về dashboard theo role.
- Dòng 73-80: role được phép gồm `customer`, `user`, `vendor`.

Ý nghĩa: cả customer lẫn vendor đều vào được `/vendor-apply`, còn chưa login thì luôn bị chuyển qua login kèm redirect.

---

## 5) `VendorApplyServlet` chi tiết

File: `VendorApplyServlet.java`

### 5.1 Hằng số phí
- Dòng 16: `VENDOR_APPLY_FEE_USD = 5.0`.

### 5.2 `doGet` (`19-36`)
- Dòng 20-21: lấy session hiện tại và resolve user.
- Dòng 22-25: nếu chưa có user/email -> redirect `/login?redirect=/vendor-apply`.
- Dòng 27-31: nếu role hiện tại đã là `vendor` -> redirect luôn `/vendor/dashboard`.
- Dòng 33-35: set data cho view (`activePage`, `vendorApplyFee`) rồi forward trang apply.

### 5.3 `doPost` (`39-63`)
- Dòng 40-45: guard login giống `doGet`.
- Dòng 47-51: guard role vendor (vendor cũ không apply lại).
- Dòng 53-57: validate `agreePolicy` phải là `true`.
- Dòng 59: lấy active session.
- Dòng 60-61: lưu trạng thái thanh toán apply vendor:
  - `vendor_apply_pending_user_id`
  - `vendor_apply_total`
- Dòng 62: redirect sang `/create-payment`.

### 5.4 Helper
- Dòng 65-68: `resolveSessionUser` đọc object `user` từ session.

---

## 6) Tạo URL thanh toán VNPay

File: `VNPayPaymentServlet.java`

### 6.1 Nhận diện flow checkout hay vendor apply
- Dòng 29-31: đọc cả `checkout_pending_user_id` và `vendor_apply_pending_user_id` từ session.
- Dòng 33-36: nếu không có pending nào -> redirect lỗi.
- Dòng 38: `isVendorApplyFlow = vendorApplyPendingUser != null`.
- Dòng 39-41: lấy tổng tiền đúng theo flow:
  - vendor apply -> `vendor_apply_total`
  - checkout -> `checkout_total`

### 6.2 Validate amount
- Dòng 43-47: nếu total null hoặc <= 0 thì redirect lỗi.

### 6.3 Build tham số VNPay
- Dòng 49: đổi từ USD sang VND (xấp xỉ 24,000) rồi nhân 100 theo chuẩn VNPay.
- Dòng 50: tạo `txnRef` từ timestamp.
- Dòng 51: lưu `vnp_txn_ref` vào session để đối chiếu callback.
- Dòng 53-64: set các param chuẩn VNPay.
- Dòng 60: `vnp_OrderInfo` có prefix khác nhau để phân biệt vendor apply vs checkout.
- Dòng 63 + 92-95: return URL callback động theo host/port hiện tại.

### 6.4 Ký request
- Dòng 70-71: sort key để hash đúng chuẩn.
- Dòng 73-84: dựng chuỗi `hashData` và `query` theo thứ tự đã sort.
- Dòng 86: tạo `vnp_SecureHash` bằng HMAC-SHA512.
- Dòng 87: append hash vào query.
- Dòng 89: redirect sang URL VNPay sandbox.

### 6.5 Config/util dùng trong bước ký

#### `VNPayConfig.java`
- Dòng 14: `vnp_TmnCode`.
- Dòng 15: `vnp_HashSecret`.
- Dòng 17-18: base URL cổng thanh toán VNPay sandbox.

#### `VNPayUtil#hmacSHA512` (`VNPayUtil.java:17-40`)
- Dòng 20-25: khởi tạo Mac HmacSHA512 với secret key.
- Dòng 27: ký dữ liệu bytes UTF-8.
- Dòng 31-33: chuyển kết quả sang chuỗi hex.

---

## 7) Callback xử lý kết quả và nâng role Vendor

File: `VNPayReturnServlet.java`

### 7.1 Đọc callback và kiểm tra hợp lệ
- Dòng 28-34: lấy session, user hiện tại, response code, transaction status, amount, txnRef.
- Dòng 36-40: điều kiện `success` gồm:
  - có user trong session,
  - `txnRef` callback trùng `vnp_txn_ref` trong session,
  - `vnp_ResponseCode = 00`,
  - `vnp_TransactionStatus = 00`.
- Dòng 42: nhận diện đây có phải vendor apply flow hay không bằng `vendor_apply_pending_user_id`.

### 7.2 Nhánh success + vendor apply (`46-69`)
- Dòng 47: lấy `role_id` của role `Vendor`.
- Dòng 48-50: nếu không có role Vendor trong DB -> lỗi servlet.
- Dòng 52: update role trong DB (`users.role_id`).
- Dòng 53-54: update object user trong memory/session thành Vendor.
- Dòng 55-58: set lại session keys `user`, `roleName` để UI/filter nhận role mới ngay.
- Dòng 60-64: tạo notification cho user: “Become Vendor successful”.
- Dòng 66: redirect đến `/vendor/dashboard`.
- Dòng 67: clear trạng thái thanh toán trong session.
- Dòng 68: `return` kết thúc nhánh.

### 7.3 Nhánh success nhưng không phải vendor apply (`71-83`)
- Đây là luồng checkout cart, không thuộc apply vendor.

### 7.4 Nhánh thất bại (`89-93`)
- Set message thất bại, success=false, clear session state thanh toán.

### 7.5 Xóa session state thanh toán
- Dòng 98-107 (`clearCheckoutSession`): xóa cả state checkout và vendor apply:
  - `checkout_total`, `checkout_pending_user_id`
  - `vendor_apply_total`, `vendor_apply_pending_user_id`
  - `vnp_txn_ref`

---

## 8) DAO liên quan trực tiếp trong vendor apply

### 8.1 `UserDao#getRoleIdByName` (`UserDao.java:36-47`)
- Query `Role` theo tên role và trả `role_id`.

### 8.2 `UserDao#updateRoleByUserId` (`UserDao.java:108-115`)
- Update `users.role_id` theo `user_id`.
- Đây là dòng DB thật sự nâng account customer lên vendor.

### 8.3 `NotificationDao#insertNotification` (`NotificationDao.java:255-266`)
- Insert bản ghi thông báo đơn giản (title/content) cho user vừa nâng role.

---

## 9) Session keys của luồng apply vendor

| Key | Giai đoạn set | Ý nghĩa |
|---|---|---|
| `vendor_apply_pending_user_id` | `VendorApplyServlet#doPost` | Đánh dấu user đang ở flow apply vendor |
| `vendor_apply_total` | `VendorApplyServlet#doPost` | Tổng tiền apply vendor (USD) |
| `vnp_txn_ref` | `VNPayPaymentServlet#doGet` | Mã giao dịch để chống callback giả/sai phiên |
| `user` | `VNPayReturnServlet` success | User object cập nhật role Vendor |
| `roleName` | `VNPayReturnServlet` success | Role hiển thị và phân quyền realtime |

---

## 10) Điểm kiểm soát an toàn hiện có

1. Guard login ở cả filter và servlet.
2. Guard “đã là vendor” ở cả GET/POST của `VendorApplyServlet`.
3. Bắt buộc đồng ý policy (backend check lại `agreePolicy=true`).
4. Callback VNPay chỉ coi là thành công khi:
   - response/status đều `00`,
   - `txnRef` callback trùng `vnp_txn_ref` trong session,
   - có user trong session.
5. Luôn clear session state thanh toán sau khi xử lý xong.

---

## 11) Ghi chú kỹ thuật khi bạn review tiếp

- `VNPayConfig` đang để `tmnCode` và `hashSecret` cứng trong code (`VNPayConfig.java:14-15`), nên chuyển sang biến môi trường/config ngoài source.
- Luồng thất bại của vendor apply hiện forward về `payment-result.jsp` (nút điều hướng đang thiên về cart), bạn có thể tách riêng UI cho vendor apply fail để UX rõ hơn.
- Tỉ giá `24000` trong `VNPayPaymentServlet` đang hard-code; có thể đưa vào config để dễ thay đổi.
