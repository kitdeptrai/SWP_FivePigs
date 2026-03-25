<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>OTP Verification - Registration</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
  <style>
    .otp-inputs {
      display: flex;
      justify-content: center;
      gap: 12px;
      margin: 30px 0;
    }
    .otp-input {
      width: 45px;
      height: 50px;
      text-align: center;
      font-size: 1.5em;
      font-weight: 600;
      border-radius: 12px;
      border: 2px solid var(--border);
      background: rgba(255, 255, 255, 0.05);
      color: var(--text);
      transition: all 0.2s ease;
    }
    .otp-input:focus {
      border-color: var(--primary);
      background: rgba(124, 58, 237, 0.1);
      outline: none;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(124, 58, 237, 0.2);
    }
    .otp-input.filled {
      border-color: var(--primary-2);
    }
    .resend-container {
        display: flex;
        justify-content: center;
        gap: 15px;
        margin-top: 25px;
        padding-top: 20px;
        border-top: 1px solid var(--border);
    }
    .resend-link {
        color: var(--primary);
        text-decoration: none;
        font-size: 14px;
        font-weight: 500;
        transition: color 0.2s;
    }
    .resend-link:hover {
        color: #a78bfa;
        text-decoration: underline;
    }
    .error-msg {
        display: none;
        margin-bottom: 15px;
    }
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        25% { transform: translateX(-5px); }
        75% { transform: translateX(5px); }
    }
    .shake {
        animation: shake 0.2s ease-in-out 0s 2;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="card">
      <div style="text-align: center; margin-bottom: 20px;">
          <div style="background: rgba(124, 58, 237, 0.1); width: 60px; height: 60px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 15px;">
              <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="var(--primary)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="8.5" cy="7" r="4"></circle><polyline points="17 11 19 13 23 9"></polyline></svg>
          </div>
          <h1>Registration Verification</h1>
          <p class="subtitle">The OTP code has been sent to email:<br><strong style="color: var(--text)"><c:out value="${sessionScope.reg_email}"/></strong></p>
      </div>

      <div id="js-error" class="alert danger error-msg"></div>

      <c:if test="${not empty error}">
        <div class="alert danger">${error}</div>
      </c:if>

      <c:if test="${param.resend == 'true'}">
        <div class="alert success">A new OTP code has been sent to your email.</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/verify-register-otp" autocomplete="off">
        <div class="otp-inputs" id="otp-container">
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" pattern="\d*" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" pattern="\d*" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" pattern="\d*" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" pattern="\d*" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" pattern="\d*" />
            <input class="otp-input" type="text" inputmode="numeric" maxlength="1" pattern="\d*" />
        </div>
        <input type="hidden" name="otp" id="otp-hidden-input" />

        <div class="actions">
          <button type="submit" id="btn-submit" style="height: 48px; font-size: 16px;">Complete Registration</button>
        </div>
      </form>

      <div class="resend-container">
          <a class="resend-link" href="${pageContext.request.contextPath}/register">Back to Registration</a>
          <span style="color: var(--border)">|</span>
          <a class="resend-link" href="${pageContext.request.contextPath}/resend-otp">Resend OTP Code</a>
      </div>
    </div>
  </div>

  <script>
    const otpContainer = document.getElementById('otp-container');
    const hiddenInput = document.getElementById('otp-hidden-input');
    const jsError = document.getElementById('js-error');
    const form = document.querySelector('form');
    const inputs = otpContainer.querySelectorAll('.otp-input');

    // Focus the first input when the page loads
    window.onload = () => inputs[0].focus();

    inputs.forEach((input, index) => {
        // Handle numeric input
        input.addEventListener('input', (e) => {
            const value = e.target.value;

            if (!/^\d$/.test(value)) {
                e.target.value = '';
                return;
            }

            input.classList.add('filled');

            if (value !== '' && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
            updateHiddenInput();
        });

        // Handle delete (Backspace)
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace') {
                if (input.value === '' && index > 0) {
                    inputs[index - 1].focus();
                    inputs[index - 1].value = '';
                    inputs[index - 1].classList.remove('filled');
                } else {
                    input.classList.remove('filled');
                }
            }
        });

        // Handle OTP paste
        input.addEventListener('paste', (e) => {
            e.preventDefault();
            const data = e.clipboardData.getData('text').trim();
            if (!/^\d{6}$/.test(data)) return;

            data.split('').forEach((char, i) => {
                if (inputs[i]) {
                    inputs[i].value = char;
                    inputs[i].classList.add('filled');
                }
            });
            updateHiddenInput();
            inputs[5].focus();
        });
    });

    function updateHiddenInput() {
        let otp = '';
        inputs.forEach(input => { otp += input.value; });
        hiddenInput.value = otp;
        if (otp.length === 6) jsError.style.display = 'none';
    }

    form.addEventListener('submit', (e) => {
        updateHiddenInput();
        if (hiddenInput.value.length !== 6) {
            e.preventDefault();
            jsError.textContent = "Please enter all 6 digits of the verification code.";
            jsError.style.display = 'block';
            otpContainer.classList.add('shake');
            setTimeout(() => otpContainer.classList.remove('shake'), 400);
        }
    });
  </script>
</body>
</html>
