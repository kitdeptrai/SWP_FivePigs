<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FIVEPIGS - About</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="about" />
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

    <div class="content-scroll-area" style="padding:24px;">
        <div style="max-width:980px;margin:0 auto;display:flex;flex-direction:column;gap:18px;">
            <section style="background:#fff;border-radius:18px;padding:28px;box-shadow:0 4px 16px rgba(15,23,42,0.05);">
                <span style="display:inline-flex;align-items:center;gap:8px;padding:8px 12px;border-radius:999px;background:#eef2ff;color:#4f46e5;font-weight:700;font-size:13px;">
                    <i class="fa-solid fa-store"></i> About FIVEPIGS
                </span>
                <h1 style="margin:16px 0 12px;font-size:34px;line-height:1.2;">A simple marketplace for apps and games</h1>
                <p style="margin:0;color:#5b647d;line-height:1.8;font-size:16px;">
                    FIVEPIGS is a web marketplace where users can explore software, choose a pricing plan, download owned products,
                    and manage everything from one library. The goal of the platform is to keep buying and using software simple:
                    browse clearly, choose the right plan, then access your products from your account when you need them.
                </p>
            </section>

            <section style="background:#fff;border-radius:18px;padding:28px;box-shadow:0 4px 16px rgba(15,23,42,0.05);">
                <h2 style="margin:0 0 14px;font-size:24px;">How purchasing works</h2>
                <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:14px;">
                    <div style="border:1px solid #e9ecf5;border-radius:16px;padding:18px;">
                        <strong style="display:block;margin-bottom:8px;">1. Choose a product</strong>
                        <p style="margin:0;color:#5b647d;line-height:1.7;">Open the product page, review the information, and select the pricing plan that fits your needs.</p>
                    </div>
                    <div style="border:1px solid #e9ecf5;border-radius:16px;padding:18px;">
                        <strong style="display:block;margin-bottom:8px;">2. Add to cart</strong>
                        <p style="margin:0;color:#5b647d;line-height:1.7;">Add the selected plan to your cart, complete checkout, and the product will be added to your Library.</p>
                    </div>
                    <div style="border:1px solid #e9ecf5;border-radius:16px;padding:18px;">
                        <strong style="display:block;margin-bottom:8px;">3. Access in Library</strong>
                        <p style="margin:0;color:#5b647d;line-height:1.7;">After purchase, you can download the product again later, review it, and manage access if the plan supports multiple users.</p>
                    </div>
                </div>
            </section>

            <section style="background:#fff;border-radius:18px;padding:28px;box-shadow:0 4px 16px rgba(15,23,42,0.05);">
                <h2 style="margin:0 0 14px;font-size:24px;">Demo and trial policy</h2>
                <p style="margin:0 0 12px;color:#5b647d;line-height:1.8;">
                    Some products may provide a demo or trial option before purchase. When available, this gives users a chance to
                    test compatibility and basic experience before committing to a paid plan.
                </p>
                <p style="margin:0;color:#5b647d;line-height:1.8;">
                    Demo and trial availability depends on the product configuration. If a product does not offer a demo or trial,
                    users should rely on the product description, screenshots, plan details, and reviews before purchasing.
                </p>
            </section>

            <section style="background:#fff7e6;border:1px solid #f3d08b;border-radius:18px;padding:28px;box-shadow:0 4px 16px rgba(15,23,42,0.04);">
                <h2 style="margin:0 0 14px;font-size:24px;color:#9a6700;">No refund policy</h2>
                <p style="margin:0 0 12px;color:#7a5d1b;line-height:1.8;">
                    FIVEPIGS does not support refunds after a purchase is completed. Because of that, users are encouraged to review
                    product details carefully and use any available demo or trial before buying.
                </p>
                <p style="margin:0;color:#7a5d1b;line-height:1.8;">
                    By completing checkout, the buyer accepts that the order is final. The product will be delivered to the Library
                    based on the selected plan, and access will follow that plan's rules and duration.
                </p>
            </section>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
