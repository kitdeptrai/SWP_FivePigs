<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="activeMenu" value="guidelines" />

<!DOCTYPE html>
<html>
    <head>
        <title>Review Guidelines</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/reviewer/reviewGuideline.css">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>

        <div class="layout">

            <%@ include file="layout/sidebar.jsp" %>

            <main class="main">

                <div class="page-header">
                    <div>
                        <h1 class="page-title">Review Guidelines</h1>
                        <p class="subtitle">Comprehensive checklist and standards for software review</p>
                    </div>

                    <div class="header-actions">
                        <button type="button" class="btn btn-primary" onclick="openGuidelineModal('add')">
                            <i class="fa-solid fa-plus"></i> Add Guideline
                        </button>

                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reviewer_guidelines_export_pdf">
                            <i class="fa-solid fa-file-arrow-down"></i> Export PDF
                        </a>
                    </div>
                </div>

                <form class="toolbar" method="get" action="${pageContext.request.contextPath}/reviewer_guidelines">
                    <div class="search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input name="keyword" value="${keyword}" placeholder="Search guidelines..." />
                    </div>

                    <div class="filter-box">
                        <i class="fa-solid fa-filter"></i>
                        <select name="category" onchange="this.form.submit()">
                            <option value="" ${empty category ? 'selected' : ''}>All Categories</option>
                            <c:forEach var="c" items="${categories}">
                                <option value="${c}" ${c == category ? 'selected' : ''}>${c}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-secondary">Search</button>
                </form>

                <c:set var="st" value="${stats}" />
                <div class="stats-row">
                    <div class="stat-card">
                        <div class="stat-icon critical"><i class="fa-solid fa-triangle-exclamation"></i></div>
                        <div>
                            <div class="stat-title">Critical</div>
                            <div class="stat-value">${st.criticalCount}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon high"><i class="fa-solid fa-circle-exclamation"></i></div>
                        <div>
                            <div class="stat-title">High Priority</div>
                            <div class="stat-value">${st.highCount}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon total"><i class="fa-regular fa-circle-check"></i></div>
                        <div>
                            <div class="stat-title">Total Items</div>
                            <div class="stat-value">${st.totalItems}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon cat"><i class="fa-solid fa-book"></i></div>
                        <div>
                            <div class="stat-title">Categories</div>
                            <div class="stat-value">${st.categoryCount}</div>
                        </div>
                    </div>
                </div>

                <h2 class="section-title">Review Guidelines</h2>

                <c:if test="${empty guidelines}">
                    <div class="empty-state">No guidelines found.</div>
                </c:if>

                <div class="guideline-list">
                    <c:forEach var="g" items="${guidelines}">
                        <div class="guideline-card">

                            <div class="g-left">
                                <div class="g-icon color-${g.color}">
                                    <c:choose>
                                        <c:when test="${g.icon == 'Shield'}"><i class="fa-solid fa-shield"></i></c:when>
                                        <c:when test="${g.icon == 'File' || g.icon == 'FileText'}"><i class="fa-regular fa-file-lines"></i></c:when>
                                        <c:when test="${g.icon == 'Code'}"><i class="fa-solid fa-code"></i></c:when>
                                        <c:when test="${g.icon == 'Bolt' || g.icon == 'Zap'}"><i class="fa-solid fa-bolt"></i></c:when>
                                        <c:when test="${g.icon == 'Check' || g.icon == 'CheckCircle'}"><i class="fa-solid fa-circle-check"></i></c:when>
                                        <c:when test="${g.icon == 'AlertTriangle'}"><i class="fa-solid fa-triangle-exclamation"></i></c:when>
                                        <c:otherwise><i class="fa-regular fa-bookmark"></i></c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="g-info">
                                    <div class="g-title-row">
                                        <h3 class="g-title">${g.title}</h3>

                                        <span class="badge badge-priority ${g.priority}">
                                            ${g.priority}
                                        </span>

                                        <span class="badge badge-category">
                                            ${g.category}
                                        </span>
                                    </div>

                                    <p class="g-desc">${g.description}</p>

                                    <a href="javascript:void(0)"
                                       class="g-toggle"
                                       id="toggle-${g.guidelineId}"
                                       onclick="toggleItems(${g.guidelineId})">
                                        View ${g.itemCount} checklist items
                                    </a>

                                    <div class="g-items" id="items-${g.guidelineId}" style="display:none;">
                                        <div class="items-loading">Loading...</div>
                                    </div>
                                </div>
                            </div>

                            <div class="g-actions">
                                <button type="button"
                                        class="icon-btn"
                                        title="Edit"
                                        data-id="${g.guidelineId}"
                                        data-category="${fn:escapeXml(g.category)}"
                                        data-priority="${fn:escapeXml(g.priority)}"
                                        data-title="${fn:escapeXml(g.title)}"
                                        data-description="${fn:escapeXml(g.description)}"
                                        data-icon="${fn:escapeXml(g.icon)}"
                                        data-color="${fn:escapeXml(g.color)}"
                                        onclick="openGuidelineModal('edit', this)">
                                    <i class="fa-regular fa-pen-to-square"></i>
                                </button>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/reviewer_guideline_delete"
                                      onsubmit="return confirm('Delete this guideline?')">
                                    <input type="hidden" name="guidelineId" value="${g.guidelineId}">
                                    <input type="hidden" name="keyword" value="${keyword}">
                                    <input type="hidden" name="category" value="${category}">
                                    <button class="icon-btn danger" type="submit" title="Delete">
                                        <i class="fa-regular fa-trash-can"></i>
                                    </button>
                                </form>
                            </div>

                        </div>
                    </c:forEach>
                </div>

                <div id="guidelineModal" class="modal" style="display:none;">
                    <div class="modal-content guideline-modal">
                        <span class="close-btn" onclick="closeGuidelineModal()">&times;</span>

                        <h2 id="modalTitle">Add New Guideline</h2>

                        <form id="guidelineForm" method="post" action="${pageContext.request.contextPath}/reviewer_guideline_create">
                            <input type="hidden" name="guidelineId" id="guidelineId" value="">

                            <div class="modal-row">
                                <input class="inp" name="category" id="fCategory" placeholder="Category (Security/Legal...)" required>

                                <select class="inp inp-select" name="priority" id="fPriority" required>
                                    <option value="">Priority</option>
                                    <option value="Critical">Critical</option>
                                    <option value="High">High</option>
                                    <option value="Medium">Medium</option>
                                    <option value="Low">Low</option>
                                </select>
                            </div>

                            <input class="inp" name="title" id="fTitle" placeholder="Title" required>

                            <textarea class="inp" name="description" id="fDescription" rows="4" placeholder="Description"></textarea>

                            <div class="modal-row">
                                <select class="inp inp-select" name="icon" id="fIcon">
                                    <option value="">Icon</option>
                                    <option value="Shield">Shield</option>
                                    <option value="File">File</option>
                                    <option value="FileText">FileText</option>
                                    <option value="Code">Code</option>
                                    <option value="Bolt">Bolt</option>
                                    <option value="Zap">Zap</option>
                                    <option value="Check">Check</option>
                                    <option value="CheckCircle">CheckCircle</option>
                                    <option value="AlertTriangle">AlertTriangle</option>
                                </select>

                                <select class="inp inp-select" name="color" id="fColor">
                                    <option value="">Color</option>
                                    <option value="Red">Red</option>
                                    <option value="Yellow">Yellow</option>
                                    <option value="Blue">Blue</option>
                                    <option value="Purple">Purple</option>
                                    <option value="Green">Green</option>
                                    <option value="Orange">Orange</option>
                                </select>
                            </div>

                            <div class="checklist-editor">
                                <div class="checklist-head">
                                    <strong>Checklist items</strong>
                                </div>

                                <div class="checklist-add-row">
                                    <input type="text" id="newItemText" class="inp" placeholder="Add checklist item...">
                                    <button type="button" class="plus-btn" onclick="addChecklistItem()">
                                        <i class="fa-solid fa-plus"></i>
                                    </button>
                                </div>

                                <div id="checklistList" class="checklist-list"></div>
                                <div id="checklistHidden"></div>
                            </div>

                            <div class="modal-actions">
                                <button type="button" class="btn btn-secondary" onclick="closeGuidelineModal()">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save</button>
                            </div>
                        </form>

                    </div>
                </div>

            </main>
        </div>

        <script>
            const BASE = "${pageContext.request.contextPath}";
            let modalItems = [];

            async function toggleItems(guidelineId) {
                const box = document.getElementById("items-" + guidelineId);
                const link = document.getElementById("toggle-" + guidelineId);

                const willShow = (box.style.display === "none" || box.style.display === "");
                box.style.display = willShow ? "block" : "none";

                if (!willShow) {
                    if (link) link.innerText = "View checklist items";
                    return;
                }

                if (link) link.innerText = "Hide checklist";

                if (box.dataset.loaded === "1") return;

                box.innerHTML = '<div class="items-loading">Loading...</div>';

                try {
                    const url = BASE + "/reviewer_guideline_items?guidelineId=" + guidelineId;
                    const res = await fetch(url);

                    if (!res.ok) {
                        box.innerHTML = '<div class="items-empty">Failed to load (' + res.status + ').</div>';
                        return;
                    }

                    const items = await res.json();

                    if (!items || items.length === 0) {
                        box.innerHTML = '<div class="items-empty">No checklist items.</div>';
                    } else {
                        box.innerHTML = items.map(it =>
                            '<div class="check-item">' +
                            '<i class="fa-regular fa-circle-check"></i>' +
                            '<span>' + escapeHtml(it.itemText) + '</span>' +
                            '</div>'
                        ).join("");
                    }

                    box.dataset.loaded = "1";
                } catch (e) {
                    console.error(e);
                    box.innerHTML = '<div class="items-empty">Error loading checklist.</div>';
                }
            }

            function escapeHtml(s) {
                return (s || "")
                    .replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll('"', "&quot;")
                    .replaceAll("'", "&#039;");
            }

            function resetGuidelineForm() {
                document.getElementById("guidelineId").value = "";
                document.getElementById("fCategory").value = "";
                document.getElementById("fPriority").value = "";
                document.getElementById("fTitle").value = "";
                document.getElementById("fDescription").value = "";
                document.getElementById("fIcon").value = "";
                document.getElementById("fColor").value = "";
                document.getElementById("newItemText").value = "";

                modalItems = [];
                renderModalItems();
            }

            async function openGuidelineModal(mode, btn = null) {
                const modal = document.getElementById("guidelineModal");
                const title = document.getElementById("modalTitle");
                const form = document.getElementById("guidelineForm");

                resetGuidelineForm();

                if (mode === "add") {
                    title.innerText = "Add New Guideline";
                    form.action = BASE + "/reviewer_guideline_create";
                    modal.style.display = "flex";
                    return;
                }

                title.innerText = "Edit Guideline";
                form.action = BASE + "/reviewer_guideline_update";

                const guidelineId = btn.dataset.id || "";

                document.getElementById("guidelineId").value = guidelineId;
                document.getElementById("fCategory").value = btn.dataset.category || "";
                document.getElementById("fPriority").value = btn.dataset.priority || "";
                document.getElementById("fTitle").value = btn.dataset.title || "";
                document.getElementById("fDescription").value = btn.dataset.description || "";
                document.getElementById("fIcon").value = btn.dataset.icon || "";
                document.getElementById("fColor").value = btn.dataset.color || "";

                modal.style.display = "flex";

                try {
                    const url = BASE + "/reviewer_guideline_items?guidelineId=" + guidelineId;
                    const res = await fetch(url);

                    if (!res.ok) {
                        console.error("Failed to load checklist items:", res.status);
                        return;
                    }

                    const items = await res.json();
                    modalItems = Array.isArray(items)
                        ? items.map(it => (it.itemText || "").trim()).filter(t => t.length > 0)
                        : [];

                    renderModalItems();
                } catch (e) {
                    console.error("Error loading checklist items", e);
                }
            }

            function closeGuidelineModal() {
                document.getElementById("guidelineModal").style.display = "none";
            }

            window.onclick = function (event) {
                const modal = document.getElementById("guidelineModal");
                if (event.target === modal) {
                    closeGuidelineModal();
                }
            };

            function renderModalItems() {
                const list = document.getElementById("checklistList");
                const hidden = document.getElementById("checklistHidden");

                list.innerHTML = "";
                hidden.innerHTML = "";

                modalItems.forEach((text, idx) => {
                    const row = document.createElement("div");
                    row.className = "check-row";
                    row.innerHTML =
                        '<div class="check-left">' +
                            '<i class="fa-regular fa-circle-check"></i>' +
                            '<span>' + escapeHtml(text) + '</span>' +
                        '</div>' +
                        '<button type="button" class="remove-btn" title="Remove" onclick="removeChecklistItem(' + idx + ')">' +
                            '<i class="fa-solid fa-xmark"></i>' +
                        '</button>';

                    list.appendChild(row);

                    const inp = document.createElement("input");
                    inp.type = "hidden";
                    inp.name = "itemText";
                    inp.value = text;
                    hidden.appendChild(inp);
                });
            }

            function addChecklistItem() {
                const inp = document.getElementById("newItemText");
                const v = (inp.value || "").trim();

                if (!v) return;

                modalItems.push(v);
                inp.value = "";
                renderModalItems();
            }

            function removeChecklistItem(index) {
                modalItems.splice(index, 1);
                renderModalItems();
            }
        </script>

    </body>
</html>