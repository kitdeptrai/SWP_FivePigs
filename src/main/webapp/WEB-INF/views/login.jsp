<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" />
</head>
<body>
<div class="container">
    <div class="card">
        <h1>Login</h1>
        <p class="subtitle">Enter your credentials to access the system</p>

        <c:if test="${param.reset == 'success'}">
            <div class="alert success">Password reset successful. Please log in.</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login" autocomplete="off">
            <input type="hidden" name="redirect" value="${redirect != null ? redirect : param.redirect}" />

            <div class="field">
                <label for="email">Email</label>
                <input id="email" name="email" type="email" required maxlength="100"
                       value="<c:out value='${email}'/>" />
            </div>

            <div class="field">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" required maxlength="72" />
            </div>

            <div class="field">
                <label for="captcha">Captcha</label>
                <div id="captchaQuestion" class="alert" style="margin-bottom:8px;">${captchaQuestion}</div>
                <input id="captcha" name="captcha" type="text" required maxlength="50" placeholder="Enter captcha answer" />
                <div style="display:flex; gap:8px; margin-top:8px;">
                    <button type="button" id="btnVerifyCaptcha" style="background:#16a34a; color:#fff; border:none; border-radius:10px; padding:10px 14px; font-weight:600; cursor:pointer;">Verify captcha</button>
                    <button type="button" id="btnRefreshCaptcha" style="background:#e2e8f0; color:#334155; border:none; border-radius:10px; padding:10px 14px; font-weight:600; cursor:pointer;">Refresh captcha</button>
                </div>
                <div id="captchaMessage" class="small" style="margin-top:6px;"></div>
            </div>

            <div class="actions">
                <button type="submit">Login</button>
            </div>

            <div style="display: flex; justify-content: space-between; margin-top: 15px;">
                <a href="${pageContext.request.contextPath}/forgot-password" class="small">Forgot password?</a>
                <a href="${pageContext.request.contextPath}/register" class="small">Don’t have an account? Register</a>
            </div>
        </form>
    </div>
</div>
<script>
    (function () {
        const captchaQuestion = document.getElementById('captchaQuestion');
        const captchaInput = document.getElementById('captcha');
        const captchaMessage = document.getElementById('captchaMessage');
        const btnRefresh = document.getElementById('btnRefreshCaptcha');
        const btnVerify = document.getElementById('btnVerifyCaptcha');
        const form = document.querySelector('form');
        const ctx = 'login';

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

        btnRefresh.addEventListener('click', function () {
            refreshCaptcha();
        });

        btnVerify.addEventListener('click', function () {
            verifyCaptcha();
        });

        form.addEventListener('submit', async function (e) {
            const ok = await verifyCaptcha();
            if (!ok) {
                e.preventDefault();
            }
        });
    })();
</script>
</body>
</html>
