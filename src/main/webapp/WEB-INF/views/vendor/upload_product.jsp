<%-- 
    Document   : upload_product
    Created on : Mar 2, 2026, 7:36:49 PM
    Author     : MinhPD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Upload Software</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor.css">
        <style>
            body{
                font-family: Arial, sans-serif;
                background:#0f172a;
                color:#fff;
                margin:0;
            }

            /* Container */
            .container{
                width:100%;
                max-width:700px;
                margin:40px auto;
            }


            /* Step Header */
            .steps{
                display:flex;
                justify-content:space-between;
                margin-bottom:40px;
                position:relative;
            }

            .steps::before{
                content:'';
                position:absolute;
                top:18px;
                left:0;
                right:0;
                height:2px;
                background:#334155;
                z-index:0;
            }

            .step{
                text-align:center;
                z-index:1;
                width:33%;
            }

            .step-circle{
                width:35px;
                height:35px;
                border-radius:50%;
                background:#1e293b;
                line-height:35px;
                margin:auto;
                border:2px solid #334155;
            }

            .step.active .step-circle{
                background:#6366f1;
                border-color:#6366f1;
            }

            .step-title{
                margin-top:8px;
                font-size:14px;
            }

            /* Card */
            .card{
                display:none;
                background: linear-gradient(145deg,#1e293b,#111827);
                padding:30px;
                border-radius:14px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.4);
            }

            .card.active{
                display:block;
            }
            input, textarea, select{
                width:100%;
                padding:12px;
                margin-bottom:18px;
                background:#0f172a;
                border:1px solid #334155;
                color:#fff;
                border-radius:8px;
                transition:0.3s;
            }

            input:focus, textarea:focus, select:focus{
                border-color:#6366f1;
                box-shadow:0 0 0 2px rgba(99,102,241,0.3);
                outline:none;
            }

            textarea{
                resize:none;
            }

            /* Buttons */
            .btn-group{
                text-align:right;
            }

            button{
                padding:8px 18px;
                border:none;
                border-radius:6px;
                cursor:pointer;
            }

            .btn-next{
                background:#6366f1;
                color:white;
            }

            .btn-prev{
                background:#334155;
                color:white;
                margin-right:10px;
            }

            .btn-submit{
                background:#16a34a;
                color:white;
            }

            .file-box{
                border:2px dashed #334155;
                padding:20px;
                text-align:center;
                border-radius:8px;
                margin-bottom:15px;
            }

        </style>
    </head>
    <body>
        <div class="layout">
            <jsp:include page="layout/side_bar.jsp"/>
            <div class="main">
                <div class="container">

                    <!-- STEP HEADER -->
                    <div class="steps">
                        <div class="step active" id="step1-indicator">
                            <div class="step-circle">1</div>
                            <div class="step-title">Basic Info</div>
                        </div>

                        <div class="step" id="step2-indicator">
                            <div class="step-circle">2</div>
                            <div class="step-title">Details</div>
                        </div>

                        <div class="step" id="step3-indicator">
                            <div class="step-circle">3</div>
                            <div class="step-title">Media & Files</div>
                        </div>
                    </div>

                    <!-- STEP 1 -->
                    <div class="card active" id="step1">
                        <h3>Basic Information</h3>

                        <input type="text" id="productName" placeholder="Product Name *">
                        <input type="text" id="version" placeholder="Version *">

                        <select id="category">
                            <option value="">Select Category *</option>
                            <option>Productivity</option>
                            <option>Developer Tools</option>
                            <option>Design</option>
                        </select>

                        <select id="language">
                            <option value="">Programming Language *</option>
                            <option>Java</option>
                            <option>JavaScript</option>
                            <option>Python</option>
                        </select>

                        <div class="btn-group">
                            <button class="btn-next" onclick="nextStep(1)">Next</button>
                        </div>
                    </div>

                    <!-- STEP 2 -->
                    <div class="card" id="step2">
                        <h3>Product Details</h3>

                        <input type="number" id="price" placeholder="Price (USD) *">

                        <textarea id="description" rows="4" placeholder="Description *"></textarea>

                        <textarea id="features" rows="3" placeholder="Key Features"></textarea>

                        <textarea id="requirements" rows="2" placeholder="System Requirements"></textarea>

                        <div class="btn-group">
                            <button class="btn-prev" onclick="prevStep(2)">Previous</button>
                            <button class="btn-next" onclick="nextStep(2)">Next</button>
                        </div>
                    </div>

                    <!-- STEP 3 -->
                    <div class="card" id="step3">
                        <h3>Media & Files</h3>

                        <div class="file-box">
                            Product File *
                            <input type="file" id="productFile">
                        </div>

                        <div class="file-box">
                            Thumbnail Image *
                            <input type="file" id="thumbnail">
                        </div>

                        <div class="file-box">
                            Screenshots
                            <input type="file" multiple>
                        </div>

                        <div class="btn-group">
                            <button class="btn-prev" onclick="prevStep(3)">Previous</button>
                            <button class="btn-submit" onclick="submitForm()">Submit</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <script>

            function nextStep(step) {

                if (step === 1) {
                    // validate step 1
                    if (!document.getElementById("productName").value ||
                            !document.getElementById("version").value ||
                            !document.getElementById("category").value ||
                            !document.getElementById("language").value) {
                        alert("Please complete all required fields in Basic Info");
                        return;
                    }
                }

                if (step === 2) {
                    if (!document.getElementById("price").value ||
                            !document.getElementById("description").value) {
                        alert("Please complete required fields in Details");
                        return;
                    }
                }

                document.getElementById("step" + step).classList.remove("active");
                document.getElementById("step" + (step + 1)).classList.add("active");

                document.getElementById("step" + step + "-indicator").classList.remove("active");
                document.getElementById("step" + (step + 1) + "-indicator").classList.add("active");
            }

            function prevStep(step) {
                document.getElementById("step" + step).classList.remove("active");
                document.getElementById("step" + (step - 1)).classList.add("active");

                document.getElementById("step" + step + "-indicator").classList.remove("active");
                document.getElementById("step" + (step - 1) + "-indicator").classList.add("active");
            }

            function submitForm() {
                if (!document.getElementById("productFile").files.length ||
                        !document.getElementById("thumbnail").files.length) {
                    alert("Please upload required files");
                    return;
                }

                alert("Upload Successful!");
            }

        </script>

    </body>
</html>