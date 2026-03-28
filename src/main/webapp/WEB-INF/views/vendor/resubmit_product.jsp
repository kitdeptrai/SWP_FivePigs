<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>

        <meta charset="UTF-8">
        <title>Resubmit Software</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor/vendor.css">

        <style>

            /* Container */

            .container{
                width:100%;
                max-width:700px;
                margin:40px auto;
            }

            /* Steps */

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
                box-shadow:0 10px 30px rgba(0,0,0,0.4);
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
                background:#f59e0b;
                color:white;
            }

            /* Upload */

            .file-box{
                border:2px dashed #334155;
                padding:20px;
                text-align:center;
                border-radius:8px;
                margin-bottom:15px;
            }

            .preview-img{
                margin-top:10px;
                max-width:120px;
                border-radius:6px;
                display:none;
            }

            .file-name{
                margin-top:8px;
                font-size:13px;
                color:#94a3b8;
            }

            /* Error */

            .error-box{
                background:rgba(239,68,68,0.12);
                border:1px solid rgba(239,68,68,0.5);
                padding:12px;
                border-radius:8px;
                margin-bottom:20px;
            }

            .genre-grid{
                display:grid;
                grid-template-columns:repeat(auto-fill, minmax(140px,1fr));
                gap:14px;
                margin-bottom:20px;
            }

            .genre-card{
                background:#0f172a;
                border:1px solid #334155;
                border-radius:10px;
                padding:14px;
                text-align:center;
                cursor:pointer;
                transition:0.25s;
                position:relative;
            }

            .genre-card:hover{
                border-color:#6366f1;
                transform:translateY(-2px);
            }

            .genre-card input{
                display:none;
            }

            .genre-name{
                font-size:14px;
                color:#cbd5e1;
                font-weight:500;
            }

            .genre-card input:checked + .genre-name{
                color:#fff;
            }

            .genre-card.active{
                background:linear-gradient(145deg,#4f46e5,#6366f1);
                border-color:#6366f1;
            }
        </style>
    </head>

    <body>

        <div class="layout">

            <jsp:include page="layout/side_bar.jsp"/>

            <div class="main">
                <div class="container">

                    <h2>Resubmit Product</h2>

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

                    <c:if test="${not empty error}">
                        <div class="error-box">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            ${error}
                        </div>
                    </c:if>

                    <form method="post"
                          action="${pageContext.request.contextPath}/vendor/resubmit_product"
                          enctype="multipart/form-data">

                        <input type="hidden" name="softwareId" value="${param.softwareId != null ? param.softwareId : softwareId}">

                        <!-- STEP 1 -->

                        <div class="card active" id="step1">

                            <h3>Basic Information</h3>

                            <label>Product Name *</label>
                            <input type="text"
                                   name="productName"
                                   id="productName"
                                   value="${param.productName != null ? param.productName : software.name}">

                            <label>Version *</label>
                            <input type="text"
                                   name="version"
                                   id="version"
                                   value="${param.version != null ? param.version : software.softwareVersion.versionName}">

                            <label>Category *</label>

                            <select name="category" id="category">

                                <option value="1"
                                        ${(param.category == '1') || (param.category == null && software.category.categoryName == 'APP') ? 'selected' : ''}>
                                    APP
                                </option>

                                <option value="2"
                                        ${(param.category == '2') || (param.category == null && software.category.categoryName == 'GAME') ? 'selected' : ''}>
                                    GAME
                                </option>

                            </select>
                            <label>Genres *</label>

                            <div class="genre-grid">

                                <c:forEach var="g" items="${listGenre}">
                                    <label class="genre-card">

                                        <input type="checkbox"
                                               name="genres"
                                               value="${g.genreId}"
                                               ${fn:contains(selectedGenres, g.genreId) ? 'checked' : ''}>

                                        <span class="genre-name">${g.name}</span>

                                    </label>
                                </c:forEach>

                            </div>

                            <label>Price *</label>

                            <input type="number"
                                   name="price"
                                   id="price"
                                   value="${param.price != null ? param.price : software.price}">

                            <div class="btn-group">
                                <button type="button" class="btn-next" onclick="nextStep(1)">Next</button>
                            </div>

                        </div>

                        <!-- STEP 2 -->

                        <div class="card" id="step2">

                            <h3>Product Description</h3>

                            <label>Short Description *</label>

                            <textarea name="shortDescription" id="shortDescription" rows="2">${not empty param.shortDescription ? param.shortDescription : software.shortDescription}</textarea>

                            <label>Full Description *</label>

                            <textarea name="description" id="description" rows="4">${not empty param.description ? param.description : software.softwareDetail.description}</textarea>

                            <label>Release Note</label>

                            <textarea name="releaseNote" id="releaseNote" rows="3">${not empty param.releaseNote ? param.releaseNote : software.softwareVersion.releaseNote}</textarea>

                            <label>System Requirement</label>

                            <textarea name="systemRequire" id="systemRequire" rows="2">${not empty param.systemRequire ? param.systemRequire : software.softwareDetail.sysRequirement}</textarea>

                            <div class="btn-group">

                                <button type="button" class="btn-prev" onclick="prevStep(2)">
                                    Previous
                                </button>

                                <button type="button" class="btn-next" onclick="nextStep(2)">
                                    Next
                                </button>

                            </div>

                        </div>

                        <!-- STEP 3 -->

                        <div class="card" id="step3">

                            <h3>Media & Files</h3>

                            <div class="file-box">

                                <label>Upload New Software File (optional)</label>

                                <input type="file" name="softwareFile" id="softwareFile">

                                <div id="softwareFileName" class="file-name"></div>

                            </div>

                            <div class="file-box">

                                <label>Change Thumbnail (optional)</label>

                                <input type="file" name="thumbnail" id="thumbnail">

                                <img id="thumbPreview" class="preview-img">

                            </div>

                            <div class="file-box">

                                <label>Upload Additional Images</label>

                                <input type="file" name="additionalImages" multiple>

                            </div>

                            <div class="btn-group">

                                <button type="button" class="btn-prev" onclick="prevStep(3)">
                                    Previous
                                </button>

                                <button type="submit" class="btn-submit">
                                    Resubmit Product
                                </button>

                            </div>

                        </div>

                    </form>

                </div>
            </div>
        </div>

        <script>
            document.querySelectorAll('.genre-card input').forEach(input => {
                input.addEventListener('change', function () {
                    this.parentElement.classList.toggle('active', this.checked);
                });

                // auto active khi load (checked sẵn)
                if (input.checked) {
                    input.parentElement.classList.add('active');
                }
            });
            function nextStep(step) {

                if (step === 1) {
                    if (!productName.value || !version.value || !category.value || !price.value) {
                        alert("Please fill all required fields in Basic Information");
                        return;
                    }
                }

                if (step === 2) {
                    if (!shortDescription.value || !description.value) {
                        alert("Please complete product description");
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


            // Preview thumbnail

            document.getElementById("thumbnail").addEventListener("change", function () {

                const file = this.files[0];
                const preview = document.getElementById("thumbPreview");

                if (file) {
                    preview.src = URL.createObjectURL(file);
                    preview.style.display = "block";
                }

            });


            // Show software file name
            document.getElementById("softwareFile").addEventListener("change", function () {

                const file = this.files[0];
                const label = document.getElementById("softwareFileName");

                if (file) {
                    label.innerHTML = "Selected: " + file.name;
                }

            });


            // Disable submit button when uploading
            document.querySelector("form").addEventListener("submit", function () {

                const btn = document.querySelector(".btn-submit");

                btn.innerHTML = "Uploading...";
                btn.disabled = true;

            });


            // Auto open step when error
            window.onload = function () {

                let step = "${errorStep}";

                if (step) {

                    document.querySelectorAll(".card").forEach(c => c.classList.remove("active"));
                    document.querySelectorAll(".step").forEach(s => s.classList.remove("active"));

                    document.getElementById("step" + step).classList.add("active");
                    document.getElementById("step" + step + "-indicator").classList.add("active");

                }

            };


        </script>

    </body>
</html>