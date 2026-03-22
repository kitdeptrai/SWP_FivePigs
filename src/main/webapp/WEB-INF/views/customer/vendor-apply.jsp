<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FIVEPIGS - Become Vendor</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">

    <style>
        .card {
            background:#fff;
            border:1px solid #ececf1;
            border-radius:14px;
            padding:24px;
            max-width:900px;
            box-shadow:0 2px 12px rgba(0,0,0,0.04);
        }
        .box {
            background:#f8fafc;
            border:1px solid #e2e8f0;
            border-radius:12px;
            padding:16px;
            margin-bottom:16px;
        }
        .policy-box {
            max-height:200px;
            overflow:auto;
        }
        .btn-disabled {
            opacity:0.6;
            cursor:not-allowed;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
    <jsp:param name="activePage" value="vendorApply" />
</jsp:include>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/customer/header.jsp" />

    <div style="padding:20px;">
        <div class="card">

            <!-- Title -->
            <h2>Become a Vendor</h2>
            <p style="color:#64748b;">
                Complete the registration fee to upgrade your account and start selling your software on FIVEPIGS.
            </p>

            <!-- Fee -->
            <div class="box">
                <div style="display:flex; justify-content:space-between;">
                    <span>Vendor Registration Fee</span>
                    <div style="text-align:right;">
                        <strong style="font-size:18px;">$5</strong><br/>
                        <span style="font-size:13px; color:#64748b;">≈ 120,000 VND</span>
                    </div>
                </div>
                <div style="margin-top:8px; font-size:13px; color:#94a3b8;">
                    Payment will be processed in VND via VNPay
                </div>
            </div>

            <!-- Policy -->
            <div class="box policy-box">
                <h4>Vendor Policy</h4>
                <ul style="padding-left:18px; font-size:14px; color:#475569;">
                    <li>You must not upload software containing malware or harmful code.</li>
                    <li>You must not violate copyright or intellectual property rights.</li>
                    <li>You are responsible for the content and quality of your products.</li>
                    <li>Spam or irrelevant content is strictly prohibited.</li>
                    <li>The system reserves the right to suspend accounts that violate policies.</li>
                    <li>The registration fee is non-refundable.</li>
                </ul>
            </div>

            <!-- Checkbox -->
            <div style="margin-top:16px;">
                <label style="display:flex; gap:8px; cursor:pointer;">
                    <input type="checkbox" id="agreePolicy" name="agreePolicy">
                    I agree to the FIVEPIGS Vendor Policy
                </label>
            </div>

            <!-- Error -->
            <c:if test="${param.error == 'must_agree'}">
                <div style="color:red; margin-top:10px;">
                    You must agree to the policy before continuing.
                </div>
            </c:if>

            <!-- Form -->
            <form action="${pageContext.request.contextPath}/vendor-apply" method="post" style="margin-top:16px;">
                <input type="hidden" name="agreePolicy" id="agreeHidden" value="false">

                <button id="submitBtn" type="submit"
                        class="btn-disabled"
                        style="border:none; background:#6366f1; color:#fff; padding:12px 18px; border-radius:10px; font-weight:700;"
                        disabled>
                    Pay & Become Vendor
                </button>
            </form>

        </div>
    </div>
</div>

<script>
    const checkbox = document.getElementById("agreePolicy");
    const button = document.getElementById("submitBtn");
    const hidden = document.getElementById("agreeHidden");

    checkbox.addEventListener("change", function () {
        if (this.checked) {
            button.disabled = false;
            button.classList.remove("btn-disabled");
            hidden.value = "true";
        } else {
            button.disabled = true;
            button.classList.add("btn-disabled");
            hidden.value = "false";
        }
    });
</script>

</body>
</html>