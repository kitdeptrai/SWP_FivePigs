<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>FIVEPIGS - User Dashboard</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:wght@400;600;700;800&display=swap" rel="stylesheet">
  <style>
    * { box-sizing: border-box; font-family: 'Noto Sans', sans-serif; }
    body {
      margin: 0;
      min-height: 100vh;
      display: grid;
      place-items: center;
      background: radial-gradient(1200px 400px at 20% 0%, #dfe2ff 0%, #f4f5fb 45%, #f7f8fc 100%);
      color: #1f2937;
      padding: 20px;
    }
    .card {
      width: 100%;
      max-width: 760px;
      background: #fff;
      border: 1px solid #e8ebf5;
      border-radius: 20px;
      padding: 34px;
      box-shadow: 0 18px 38px rgba(17, 24, 39, 0.08);
    }
    h1 { margin: 0 0 10px; font-size: 34px; }
    p { margin: 0 0 18px; color: #5b6479; }
    .actions { display: flex; gap: 12px; flex-wrap: wrap; }
    .btn {
      display: inline-flex;
      text-decoration: none;
      border-radius: 12px;
      padding: 11px 18px;
      font-weight: 700;
    }
    .btn.primary { background: #6b70ff; color: #fff; }
    .btn.ghost { background: #eef1ff; color: #4d56d7; }
  </style>
</head>
<body>
  <div class="card">
    <h1>Welcome to FIVEPIGS Store</h1>
    <p>Bạn đang ở User Dashboard (chưa đăng nhập). Đăng nhập để dùng Library, Cart và các tính năng cá nhân.</p>
    <div class="actions">
      <a class="btn primary" href="${pageContext.request.contextPath}/login">Login</a>
      <a class="btn ghost" href="${pageContext.request.contextPath}/register">Create account</a>
    </div>
  </div>
</body>
</html>