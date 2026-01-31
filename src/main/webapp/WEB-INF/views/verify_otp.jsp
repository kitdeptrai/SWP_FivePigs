<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Xác thực OTP</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
  <style>
    .otp-inputs {
      display: flex;
      justify-content: center;
      gap: 10px;
      margin: 20px 0;
    }
    .otp-input {
      width: 40px;
      height: 40px;
      text-align: center;
      font-size: 1.2em;
      border-radius: 8px;
    }
    .resend-link {
        display: block;
        text-align: center;
        margin-top: 15px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="card">
      <h1>Nhập mã OTP</h1>
      <p class="subtitle">Một mã OTP đã được gửi đến email <strong><c:out value="${sessionScope.reg_email}"/></strong>. Vui lòng nhập mã vào ô dưới đây.</p>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>

      <c:if test="${param.resend == 'true'}">
        <div class="alert success">Một mã OTP mới đã được gửi đến email của bạn.</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/verify-register-otp" autocomplete="off">
        <div class="otp-inputs" id="otp-container">
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" />
        </div>
        <input type="hidden" name="otp" id="otp-hidden-input" />

        <div class="actions">
          <button type="submit">Xác thực</button>
        </div>
      </form>

      <a href="${pageContext.request.contextPath}/resend-otp" class="resend-link">Gửi lại mã OTP</a>
    </div>
  </div>

  <script>
    const otpContainer = document.getElementById('otp-container');
    const hiddenInput = document.getElementById('otp-hidden-input');
    const form = document.querySelector('form');

    otpContainer.addEventListener('input', (e) => {
        const target = e.target;
        const value = target.value;
        if (isNaN(value)) {
            target.value = '';
            return;
        }
        if (value !== '') {
            const next = target.nextElementSibling;
            if (next) {
                next.focus();
            }
        }
        updateHiddenInput();
    });

    otpContainer.addEventListener('keydown', (e) => {
        const target = e.target;
        if (e.key === 'Backspace' && target.value === '') {
            const prev = target.previousElementSibling;
            if (prev) {
                prev.focus();
            }
        }
        updateHiddenInput();
    });

    function updateHiddenInput() {
        let otp = '';
        for (const input of otpContainer.children) {
            otp += input.value;
        }
        hiddenInput.value = otp;
    }

    form.addEventListener('submit', updateHiddenInput);
  </script>
</body>
</html>
