/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function switchPage(pageId, element) {
    document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
    element.classList.add('active');
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active-section'));
    document.getElementById(pageId).classList.add('active-section');
}

function toggleList(page, type) {
    const btnT = document.getElementById(page + '-trend');
    const btnB = document.getElementById(page + '-best');
    const listT = document.getElementById(page + '-list-trend');
    const listB = document.getElementById(page + '-list-best');

    if (type === 'trend') {
        btnT.classList.add('active');
        btnB.classList.remove('active');
        listT.style.display = 'block';
        listB.style.display = 'none';
    } else {
        btnB.classList.add('active');
        btnT.classList.remove('active');
        listB.style.display = 'block';
        listT.style.display = 'none';
    }
}

/* Hàm bật tắt menu */
function toggleUserDropdown() {
    var dropdown = document.getElementById("userDropdown");
    dropdown.classList.toggle("show");
}

/* Đóng menu khi click ra ngoài */
window.onclick = function (event) {
    // Kiểm tra nếu click KHÔNG nằm trong user-profile-container
    if (!event.target.closest('.user-profile-container')) {
        var dropdowns = document.getElementsByClassName("dropdown-menu");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const reportModal = document.getElementById("libraryReportModal");
    const reportOverlay = document.getElementById("libraryReportOverlay");
    const reportSoftwareId = document.getElementById("libraryReportSoftwareId");
    const reportSubtitle = document.getElementById("libraryReportSubtitle");
    const reportReason = document.getElementById("libraryReportReason");

    function openReportModal(softwareId, softwareName) {
        if (!reportModal || !reportOverlay || !reportSoftwareId) return;
        reportSoftwareId.value = softwareId || "";
        if (reportSubtitle) {
            reportSubtitle.textContent = softwareName
                ? `Tell us what is wrong with ${softwareName}.`
                : "Tell us what is wrong with this product.";
        }
        if (reportReason) {
            reportReason.value = "";
        }
        reportModal.classList.add("active");
        reportOverlay.classList.add("active");
        reportModal.setAttribute("aria-hidden", "false");
    }

    function closeReportModal() {
        if (!reportModal || !reportOverlay) return;
        reportModal.classList.remove("active");
        reportOverlay.classList.remove("active");
        reportModal.setAttribute("aria-hidden", "true");
    }

    document.querySelectorAll(".library-report-trigger").forEach(function (trigger) {
        trigger.addEventListener("click", function () {
            openReportModal(
                trigger.dataset.softwareId || "",
                trigger.dataset.softwareName || ""
            );
        });
    });

    document.querySelectorAll("[data-close-report-modal]").forEach(function (button) {
        button.addEventListener("click", closeReportModal);
    });

    if (reportOverlay) {
        reportOverlay.addEventListener("click", closeReportModal);
    }

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            closeReportModal();
        }
    });
});
