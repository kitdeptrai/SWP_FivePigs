<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
<div class="container">
    <div class="card">
        <h1>Create account</h1>
        <p class="subtitle">Register to start using the system</p>

        <c:if test="${not empty error}">
            <div class="alert danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register" autocomplete="off">
            <div class="field">
                <label for="fullName">Full name</label>
                <input id="fullName" name="fullName" type="text" required minlength="2" maxlength="100"
                       value="<c:out value='${fullName}'/>" />
            </div>

            <div class="field">
                <label for="email">Email</label>
                <input id="email" name="email" type="email" required maxlength="120"
                       value="<c:out value='${email}'/>" />
            </div>

            <div class="row">
                <div class="field">
                    <label for="password">Password</label>
                    <input id="password" name="password" type="password" required minlength="6" maxlength="72" />
                </div>

                <div class="field">
                    <label for="confirmPassword">Confirm password</label>
                    <input id="confirmPassword" name="confirmPassword" type="password" required minlength="6" maxlength="72" />
                </div>
            </div>

            <div class="field">
                <label for="captcha">Captcha</label>
                <div id="captchaQuestion" class="alert" style="margin-bottom:8px;"><c:out value="${captchaQuestion}"/></div>
                <input id="captcha" name="captcha" type="text" required maxlength="50" placeholder="Enter captcha answer" />
                <div style="display:flex; gap:8px; margin-top:8px;">
                    <button type="button" id="btnVerifyCaptcha" style="background:#16a34a; color:#fff; border:none; border-radius:10px; padding:10px 14px; font-weight:600; cursor:pointer;">Verify captcha</button>
                    <button type="button" id="btnRefreshCaptcha" style="background:#e2e8f0; color:#334155; border:none; border-radius:10px; padding:10px 14px; font-weight:600; cursor:pointer;">Refresh captcha</button>
                </div>
                <div id="captchaMessage" class="small" style="margin-top:6px;"></div>
            </div>

            <div class="actions">
                <button type="submit">Register</button>
            </div>

            <p class="small">By registering, you agree to the terms of use.</p>
        </form>
    </div>
</div>

<script>
    (function () {
        const form = document.querySelector('form');
        const pw = document.getElementById('password');
        const cpw = document.getElementById('confirmPassword');
        const captchaQuestion = document.getElementById('captchaQuestion');
        const captchaInput = document.getElementById('captcha');
        const captchaMessage = document.getElementById('captchaMessage');
        const btnRefresh = document.getElementById('btnRefreshCaptcha');
        const btnVerify = document.getElementById('btnVerifyCaptcha');
        const ctx = 'register';

        function checkPassword() {
            if (pw.value !== cpw.value) {
                cpw.setCustomValidity('Password confirmation does not match');
            } else {
                cpw.setCustomValidity('');
            }
        }

        async function refreshCaptcha() {
            const res = await fetch('${pageContext.request.contextPath}/captcha-api?ctx=' + ctx, { method: 'GET' });
            const data = await res.json();
            if (data.ok) {
                captchaQuestion.textContent = data.question;
                captchaMessage.textContent = 'Captcha refreshed.';
                captchaMessage.style.color = '#0ea5e9';
                captchaInput.value = '';
            }
        }

        async function verifyCaptcha() {
            const body = new URLSearchParams();
            body.set('ctx', ctx);
            body.set('answer', captchaInput.value || '');

            const res = await fetch('${pageContext.request.contextPath}/captcha-api', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: body.toString()
            });
            const data = await res.json();

            captchaMessage.textContent = data.message;
            captchaMessage.style.color = data.ok ? '#16a34a' : '#dc2626';
            return data.ok;
        }

        pw.addEventListener('input', checkPassword);
        cpw.addEventListener('input', checkPassword);

        btnRefresh.addEventListener('click', function () {
            refreshCaptcha();
        });

        btnVerify.addEventListener('click', function () {
            verifyCaptcha();
        });

        form.addEventListener('submit', async function (e) {
            checkPassword();
            if (!form.checkValidity()) {
                return;
            }
            const ok = await verifyCaptcha();
            if (!ok) {
                e.preventDefault();
            }
        });
    })();
</script>
</body>
</html>
