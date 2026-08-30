# AUTH FLOW 01: Login - Register - Forgot Password (Giải Thích Chi Tiết Theo Dòng)

Tài liệu này giải thích chi tiết luồng backend cho 3 chức năng:
1. Đăng nhập
2. Đăng ký + OTP
3. Quên mật khẩu + OTP + đặt mật khẩu mới

Mục tiêu là để bạn đối chiếu trực tiếp từng dòng code trong project, theo đúng thứ tự request chạy thật.

## 1) Phạm vi file được giải thích

- `src/main/java/com/fivepigs/app/web/LoginServlet.java`
- `src/main/java/com/fivepigs/app/web/RegisterServlet.java`
- `src/main/java/com/fivepigs/app/web/VerifyRegisterOtpServlet.java`
- `src/main/java/com/fivepigs/app/web/ResendOtpServlet.java`
- `src/main/java/com/fivepigs/app/web/ForgotPasswordServlet.java`
- `src/main/java/com/fivepigs/app/web/VerifyResetOtpServlet.java`
- `src/main/java/com/fivepigs/app/web/ResetPasswordServlet.java`
- `src/main/java/com/fivepigs/app/service/UserService.java`
- `src/main/java/com/fivepigs/app/dao/UserDao.java`
- `src/main/java/com/fivepigs/app/util/PasswordUtil.java`
- `src/main/java/com/fivepigs/app/util/OtpUtil.java`
- `src/main/java/com/fivepigs/app/web/CaptchaApiServlet.java`
- `src/main/java/com/fivepigs/app/util/EmailService.java`

## 2) Luồng Đăng Nhập (Login)

### 2.1 `LoginServlet#doGet` (`LoginServlet.java:25-43`)

- Dòng 25: Bắt đầu xử lý khi user truy cập `GET /login`.
- Dòng 26: Lấy query param `redirect` và sanitize để tránh open redirect.
- Dòng 27-29: Nếu `redirect` hợp lệ thì set vào request để JSP giữ lại.
- Dòng 31: Lấy session hiện tại bằng `getSession(false)` để không tự tạo session mới.
- Dòng 32: Kiểm tra nếu session đã có `user` (đã đăng nhập).
- Dòng 33-35: Nếu có `redirect` thì ưu tiên quay về trang đó.
- Dòng 37: Nếu không có `redirect`, chuyển tới dashboard theo role.
- Dòng 41: Nếu chưa login, đảm bảo captcha đã có câu hỏi (`ctx = login`).
- Dòng 42: Forward tới `login.jsp`.

### 2.2 `LoginServlet#doPost` (`LoginServlet.java:46-95`)

- Dòng 47: Set UTF-8 để đọc form đúng encoding.
- Dòng 49-51: Đọc `email`, `password`, `redirect` từ form.
- Dòng 53-54: Gán lại email/redirect vào request để render lại form nếu lỗi.
- Dòng 56-61: Validate cơ bản: thiếu email/password -> báo lỗi và forward lại form.
- Dòng 63: Bắt buộc captcha đã được verify trước khi submit.
- Dòng 64-69: Nếu captcha chưa pass -> báo lỗi, tạo captcha mới và forward lại.
- Dòng 71-72: Gọi service `userService.login(email, password)`.
- Dòng 73-78: Nếu login fail (sai tài khoản, sai pass, hoặc status không ACTIVE) -> báo lỗi.
- Dòng 80: Lấy/khởi tạo session cho user đã đăng nhập.
- Dòng 81: Lấy role name từ `roleId`.
- Dòng 82-84: Gắn `user` và `roleName` vào session.
- Dòng 86-89: Nếu có `redirect`, quay lại URL đã sanitize.
- Dòng 91: Nếu không có `redirect`, chuyển dashboard theo role.
- Dòng 92-94: Bọc lỗi SQL thành `ServletException`.

### 2.3 `LoginServlet#redirectToDashboard` (`LoginServlet.java:97-130`)

- Dòng 99: Lấy role name theo `roleId`.
- Dòng 100-102: Nếu role null thì fallback thành `User`.
- Dòng 104: Chuẩn hóa `toLowerCase()` để so sánh switch.
- Dòng 106-125: Mapping role -> URL dashboard.
- Dòng 126: Redirect đến dashboard tương ứng.
- Dòng 127-129: Nếu lỗi SQL khi lấy role -> ném `ServletException`.

### 2.4 Hàm phụ bảo mật redirect (`LoginServlet.java:132-146`)

- `trim` (132-136): Chuẩn hóa chuỗi đầu vào, rỗng -> null.
- `sanitizeRedirect` (138-145):
  - Phải bắt đầu bằng `/`.
  - Không cho `//`.
  - Không cho chứa `://`.
  - Mục tiêu: tránh redirect sang domain bên ngoài.

### 2.5 Service + DAO của login

#### `UserService#login` (`UserService.java:54-70`)
- Dòng 55: Tìm user theo email.
- Dòng 56-58: Không tìm thấy -> trả `null`.
- Dòng 61-63: Chỉ cho phép user có `status = ACTIVE`.
- Dòng 65: Hash password input bằng SHA-256.
- Dòng 66-68: So sánh hash input với hash trong DB.
- Dòng 70: Pass hết điều kiện -> trả user.

#### `UserDao#findByEmail` (`UserDao.java:49-74`)
- Dòng 50-53: Query join `Users` + `Role` theo email.
- Dòng 55: Bind parameter email.
- Dòng 57-69: Map từng cột SQL vào object `User`.
- Dòng 73: Không có bản ghi -> trả `null`.

#### `PasswordUtil#sha256` (`PasswordUtil.java:10-22`)
- Dòng 12: Tạo `MessageDigest` SHA-256.
- Dòng 13: Hash bytes UTF-8.
- Dòng 14-17: Chuyển byte[] -> chuỗi hex.
- Dòng 18: Trả về hash.

### 2.6 Captcha áp dụng cho login

- `CaptchaApiServlet.ensureCaptcha` (`CaptchaApiServlet.java:62-75`): đảm bảo có câu hỏi captcha cho context `login`.
- `CaptchaApiServlet.requireVerified` (`CaptchaApiServlet.java:77-89`): bắt buộc đã verify captcha, nếu pass thì xóa dữ liệu captcha khỏi session để không tái sử dụng.

---

## 3) Luồng Đăng Ký + OTP

### 3.1 `RegisterServlet#doGet` (`RegisterServlet.java:28-32`)

- Dòng 30: Đảm bảo câu hỏi captcha cho context `register`.
- Dòng 31: Forward trang `register.jsp`.

### 3.2 `RegisterServlet#doPost` (`RegisterServlet.java:35-108`)

- Dòng 38: Set UTF-8.
- Dòng 40-43: Lấy fullName/email/password/confirmPassword.
- Dòng 45-46: Gắn lại fullName/email để giữ form khi báo lỗi.
- Dòng 48: Validate business rule đăng ký qua service.
- Dòng 49-54: Nếu validate fail -> báo lỗi + forward.
- Dòng 56: Bắt buộc captcha phải verified trước submit.
- Dòng 57-62: Nếu captcha fail -> báo lỗi + forward.
- Dòng 65: Check email đã tồn tại chưa.
- Dòng 66-70: Email đã tồn tại -> báo lỗi + forward.
- Dòng 74: Sinh OTP 6 số ngẫu nhiên.
- Dòng 76: Lấy session.
- Dòng 77-83: Lưu toàn bộ dữ liệu đăng ký vào session:
  - `reg_fullName`, `reg_email`, `reg_password`
  - `reg_otp` (hash OTP)
  - `reg_otp_expire` (5 phút)
  - `reg_otp_attempts` (đếm số lần nhập sai)
- Dòng 86-92: Tạo nội dung email chứa OTP.
- Dòng 95-101: Gửi email OTP bằng thread riêng để không block request.
- Dòng 103: Redirect sang `/verify-register-otp`.

### 3.3 `UserService#validateRegistration` (`UserService.java:38-52`)

- Dòng 39-41: Họ tên từ 2-100 ký tự.
- Dòng 42-44: Email 5-100 ký tự và có `@`.
- Dòng 45-47: Password từ 6-72 ký tự.
- Dòng 48-50: Confirm phải khớp password.
- Dòng 51: Hợp lệ -> trả `null`.

### 3.4 `VerifyRegisterOtpServlet#doGet` (`VerifyRegisterOtpServlet.java:21-30`)

- Dòng 23-27: Không có session đăng ký (`reg_email`) thì ép quay về `/register`.
- Dòng 28-29: Nếu hợp lệ thì forward trang nhập OTP.

### 3.5 `VerifyRegisterOtpServlet#doPost` (`VerifyRegisterOtpServlet.java:33-92`)

- Dòng 35-39: Guard session giống doGet.
- Dòng 41-45: Đọc OTP nhập vào + OTP hash trong session + expire + attempts.
- Dòng 47-52: OTP thiếu/hết hạn -> hủy session + quay về trang register với lỗi.
- Dòng 54-66: OTP sai:
  - tăng attempts,
  - sai >= 5 lần: invalidate session và bắt đăng ký lại,
  - chưa quá 5: báo số lần còn lại.
- Dòng 68-71: Lấy dữ liệu đăng ký đã lưu trong session.
- Dòng 73: Gọi `userService.registerUser(...)` để insert DB thật.
- Dòng 76-83: Gửi notification nội bộ đến admin về user mới.
- Dòng 85: Invalidate session để xóa dữ liệu nhạy cảm.
- Dòng 87: Redirect `/login?registered=1`.

### 3.6 `UserService#registerUser` (`UserService.java:21-36`)

- Dòng 22: Hash password trước khi lưu.
- Dòng 25: Lấy `role_id` của role mặc định `Customer`.
- Dòng 26-30: Nếu thiếu role mặc định trong DB -> ném SQLException.
- Dòng 33: Tạo object `User` mới (`status` mặc định ACTIVE từ constructor).
- Dòng 34: Gọi DAO insert user.

### 3.7 DAO của register

#### `UserDao#getRoleIdByName` (`UserDao.java:36-47`)
- Query role theo `role_name` và trả `role_id`.

#### `UserDao#insertUser` (`UserDao.java:24-34`)
- Insert `full_name`, `email`, `password(hash)`, `role_id`, `status`.
- Nếu `user.getStatus()` null thì fallback `ACTIVE` (dòng 31).

### 3.8 `ResendOtpServlet#doGet` (`ResendOtpServlet.java:18-52`)

- Dòng 19-26: Chỉ hoạt động khi session đăng ký còn tồn tại.
- Dòng 31: Sinh OTP mới.
- Dòng 34-36: Ghi đè OTP hash + expire + attempts.
- Dòng 39-45: Tạo email body OTP mới.
- Dòng 48: Gửi email OTP mới.
- Dòng 51: Redirect về trang verify OTP.

---

## 4) Luồng Quên Mật Khẩu + OTP + Reset Password

### 4.1 `ForgotPasswordServlet#doGet` (`ForgotPasswordServlet.java:21-23`)

- Hiển thị form nhập email quên mật khẩu.

### 4.2 `ForgotPasswordServlet#doPost` (`ForgotPasswordServlet.java:26-82`)

- Dòng 27: Đọc email từ form.
- Dòng 30-34: Email rỗng -> báo lỗi.
- Dòng 37: Check email tồn tại bằng service.
- Dòng 38-42: Không tồn tại -> báo lỗi.
- Dòng 44: Lấy session.
- Dòng 47-53: Rate limit gửi OTP: 1 lần / 60 giây / session.
- Dòng 55: Sinh OTP.
- Dòng 57-61: Lưu state reset vào session:
  - `reset_email`
  - `reset_otp_hash`
  - `reset_otp_expire` (5 phút)
  - `reset_otp_last_sent`
  - `reset_otp_attempts`
- Dòng 63-69: Tạo email body OTP reset.
- Dòng 71: Gửi email OTP async.
- Dòng 74: Set `reset_verified = false` (chưa xác thực OTP).
- Dòng 75: Redirect sang `/verify-reset-otp`.

### 4.3 `VerifyResetOtpServlet#doGet` (`VerifyResetOtpServlet.java:18-53`)

- Dòng 19-23: Nếu thiếu `reset_email` trong session -> quay `/forgot-password`.
- Dòng 26-27: Nhận cờ `resend=true` để gửi lại OTP.
- Dòng 28-31: Nếu chưa qua 60s từ lần gửi trước -> báo lỗi chờ thêm.
- Dòng 32-37: Nếu được resend, sinh OTP mới và reset attempts.
- Dòng 38-45: Tạo email body và gửi mail OTP mới.
- Dòng 46: Đặt thông báo thành công resend.
- Dòng 48-49: Forward lại trang verify reset OTP.
- Dòng 52: Trường hợp không resend thì chỉ forward trang verify.

### 4.4 `VerifyResetOtpServlet#doPost` (`VerifyResetOtpServlet.java:56-92`)

- Dòng 57-61: Guard session (thiếu state reset -> quay lại forgot).
- Dòng 63-67: Lấy OTP input + hash trong session + expire + attempts.
- Dòng 69-74: OTP hết hạn/invalid -> invalidate session, quay trang forgot.
- Dòng 76-88: OTP sai:
  - tăng attempts,
  - sai >= 5 lần thì hủy session,
  - nếu chưa quá 5 lần thì báo số lần còn lại.
- Dòng 90: OTP đúng -> set `reset_verified = true`.
- Dòng 91: Redirect `/reset-password`.

### 4.5 `ResetPasswordServlet#doGet` (`ResetPasswordServlet.java:19-33`)

- Dòng 20-24: Thiếu `reset_email` -> quay `/forgot-password`.
- Dòng 26-30: Chưa verify OTP (`reset_verified != true`) -> quay `/verify-reset-otp`.
- Dòng 32: Cho phép mở trang nhập mật khẩu mới.

### 4.6 `ResetPasswordServlet#doPost` (`ResetPasswordServlet.java:36-82`)

- Dòng 37-47: Guard session và guard `reset_verified` lần nữa.
- Dòng 49-50: Lấy password mới + confirm.
- Dòng 54-58: Validate độ dài password (6..72).
- Dòng 60-64: Validate confirm trùng khớp.
- Dòng 67: Gọi service reset password.
- Dòng 70-74: Xóa toàn bộ state OTP/reset khỏi session.
- Dòng 76: Invalidate toàn bộ session.
- Dòng 77: Redirect về login với cờ `reset=success`.

### 4.7 Service + DAO của reset password

#### `UserService#resetPassword` (`UserService.java:77-80`)
- Dòng 78: Hash password mới bằng SHA-256.
- Dòng 79: Gọi DAO update vào DB.

#### `UserDao#updatePassword` (`UserDao.java:89-96`)
- Dòng 90: SQL update password theo email.
- Dòng 92-93: Bind hash password mới và email.
- Dòng 94: Execute update.

---

## 5) Session Keys Quan Trọng Trong 3 Luồng

### 5.1 Login/Captcha

| Key | Ý nghĩa |
|---|---|
| `user` | Object user sau login thành công |
| `roleName` | Tên role để phân quyền/hiển thị |
| `login_captcha_question`... | Dữ liệu captcha context login |
| `register_captcha_question`... | Dữ liệu captcha context register |

### 5.2 Register OTP

| Key | Ý nghĩa |
|---|---|
| `reg_fullName` | Họ tên đã nhập ở form register |
| `reg_email` | Email đăng ký |
| `reg_password` | Password tạm trong session trước khi OTP pass |
| `reg_otp` | SHA-256 của OTP |
| `reg_otp_expire` | Thời điểm hết hạn OTP |
| `reg_otp_attempts` | Số lần nhập OTP sai |

### 5.3 Forgot/Reset OTP

| Key | Ý nghĩa |
|---|---|
| `reset_email` | Email cần reset password |
| `reset_otp_hash` | SHA-256 của OTP reset |
| `reset_otp_expire` | Hết hạn OTP reset |
| `reset_otp_last_sent` | Mốc thời gian gần nhất gửi OTP (rate limit) |
| `reset_otp_attempts` | Số lần nhập sai OTP reset |
| `reset_verified` | Đã xác thực OTP thành công chưa |

---

## 6) Captcha API (vì login/register phụ thuộc trực tiếp)

### 6.1 `doGet /captcha-api` (`CaptchaApiServlet.java:25-37`)
- Nhận `ctx` (`login` hoặc `register`).
- Sinh captcha mới.
- Lưu captcha vào session.
- Trả về JSON chứa câu hỏi.

### 6.2 `doPost /captcha-api` (`CaptchaApiServlet.java:40-60`)
- Nhận `ctx` + `answer`.
- Verify câu trả lời bằng `verifyCaptcha(...)`.
- Nếu đúng, set `ctx_captcha_verified = true`.

### 6.3 `verifyCaptcha` (`CaptchaApiServlet.java:91-123`)
- Từ chối nếu captcha chưa có / đã hết hạn.
- Giới hạn sai tối đa 3 lần/captcha.
- Sai thì trả thông báo lỗi.
- Đúng thì trả `ok=true`.

---

## 7) Email OTP được gửi như thế nào

### 7.1 Trong servlet

- Register/Forgot/Resend/VerifyReset đều gọi `EmailService.sendHtmlEmail(...)` trong `new Thread(...)`.
- Mục đích: không block request khi SMTP chậm.

### 7.2 `EmailService` (`EmailService.java:38-65`)

- Dòng 39-44: cấu hình SMTP Gmail.
- Dòng 46-51: tạo `Session` có auth bằng `email.from` + `email.password`.
- Dòng 54-58: dựng email HTML.
- Dòng 60: gửi mail.

---

## 8) Kết luận luồng tổng quát

1. `Login`: Form -> captcha -> `UserService.login` -> `UserDao.findByEmail` -> so sánh hash -> set session -> redirect dashboard.
2. `Register`: Form -> validate -> captcha -> check email tồn tại -> tạo OTP + lưu session -> verify OTP -> `registerUser` -> insert DB.
3. `Forgot/Reset`: nhập email -> gửi OTP + rate limit -> verify OTP -> set `reset_verified` -> đổi password (hash) -> xóa session state.

---

## 9) Ghi chú kỹ thuật để bạn lưu ý khi review code

- Hiện tại password/OTP đang hash SHA-256 (không salt riêng), dùng được nhưng nên nâng cấp BCrypt/Argon2 để an toàn hơn.
- Một số chuỗi tiếng Việt trong vài file đang bị lỗi encoding hiển thị (mojibake), không ảnh hưởng logic nhưng nên chuẩn hóa UTF-8.
- `new Thread(...)` gửi mail trực tiếp trong servlet hoạt động được, nhưng production thường nên dùng queue/executor để quản lý tốt hơn.
