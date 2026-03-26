<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Error Reports – Approval</title>
            <link rel="stylesheet" href="/assets/css/Approval/approval.css">
            <style>
                /* ──────────────────────────────
           Layout
        ────────────────────────────── */
                .content-errors {
                    background: #0f1220;
                    min-height: 100vh;
                    padding: 48px 56px;
                    margin-left: 260px;
                    width: calc(100% - 260px);
                }

                .errors-wrap {
                    max-width: 1120px;
                    margin: 0 auto;
                }

                /* ──────────────────────────────
           Header
        ────────────────────────────── */
                .page-heading {
                    margin-bottom: 32px;
                }

                .page-heading h1 {
                    font-size: 28px;
                    font-weight: 800;
                    color: #f3f6ff;
                }

                .page-heading p {
                    margin-top: 6px;
                    font-size: 14px;
                    color: #64748b;
                }

                /* ──────────────────────────────
           Filter bar
        ────────────────────────────── */
                .filter-bar {
                    display: flex;
                    gap: 12px;
                    margin-bottom: 28px;
                    flex-wrap: wrap;
                }

                .filter-bar input,
                .filter-bar select {
                    flex: 1;
                    min-width: 160px;
                    background: #14182b;
                    border: 1px solid #1f2538;
                    border-radius: 10px;
                    color: #e2e8f0;
                    padding: 10px 14px;
                    font-size: 13px;
                    outline: none;
                    transition: border-color .2s;
                }

                .filter-bar input::placeholder {
                    color: #475569;
                }

                .filter-bar input:focus,
                .filter-bar select:focus {
                    border-color: #4f6cff;
                }

                /* ──────────────────────────────
           Stats strip
        ────────────────────────────── */
                .stats-strip {
                    display: flex;
                    gap: 14px;
                    margin-bottom: 28px;
                }

                .stat-chip {
                    background: #14182b;
                    border: 1px solid #1f2538;
                    border-radius: 12px;
                    padding: 14px 22px;
                    text-align: center;
                    flex: 1;
                }

                .stat-chip .num {
                    font-size: 26px;
                    font-weight: 800;
                    color: #f3f6ff;
                }

                .stat-chip .lbl {
                    font-size: 12px;
                    color: #64748b;
                    margin-top: 4px;
                }

                .stat-chip.danger .num {
                    color: #ef4444;
                }

                .stat-chip.warn .num {
                    color: #f59e0b;
                }

                /* ──────────────────────────────
           Table
        ────────────────────────────── */
                .err-table-wrap {
                    background: #14182b;
                    border: 1px solid #1f2538;
                    border-radius: 18px;
                    overflow: hidden;
                }

                table.err-table {
                    width: 100%;
                    border-collapse: collapse;
                    font-size: 14px;
                }

                .err-table thead th {
                    padding: 14px 18px;
                    text-align: left;
                    color: #64748b;
                    font-weight: 700;
                    font-size: 11px;
                    text-transform: uppercase;
                    letter-spacing: .6px;
                    border-bottom: 1px solid #1f2538;
                    background: rgba(255, 255, 255, .02);
                }

                .err-table tbody tr {
                    border-bottom: 1px solid #1e2540;
                    transition: background .15s;
                }

                .err-table tbody tr:last-child {
                    border-bottom: none;
                }

                .err-table tbody tr:hover {
                    background: rgba(79, 108, 255, .05);
                }

                .err-table tbody td {
                    padding: 16px 18px;
                    vertical-align: middle;
                    color: #cbd5e1;
                }

                .err-table td.app-col {
                    font-weight: 700;
                    color: #f3f6ff;
                }

                .err-table td.date-col {
                    font-size: 13px;
                    color: #475569;
                }

                .err-table td.reason-col {
                    max-width: 280px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    color: #94a3b8;
                    font-size: 13px;
                }

                /* Bug confirmed badge */
                .badge-bug {
                    display: inline-flex;
                    align-items: center;
                    gap: 5px;
                    padding: 4px 12px;
                    border-radius: 999px;
                    font-size: 12px;
                    font-weight: 700;
                    white-space: nowrap;
                }

                .badge-bug.confirmed {
                    background: rgba(239, 68, 68, .15);
                    color: #ef4444;
                    border: 1px solid rgba(239, 68, 68, .35);
                }

                .badge-bug.unknown {
                    background: rgba(148, 163, 184, .08);
                    color: #94a3b8;
                    border: 1px solid rgba(148, 163, 184, .25);
                }

                /* Action button */
                .btn-decide {
                    display: inline-block;
                    background: linear-gradient(135deg, #ef4444, #b91c1c);
                    padding: 8px 18px;
                    border-radius: 8px;
                    color: #fff;
                    font-size: 13px;
                    font-weight: 600;
                    text-decoration: none;
                    transition: .2s;
                    white-space: nowrap;
                }

                .btn-decide:hover {
                    box-shadow: 0 6px 16px rgba(239, 68, 68, .4);
                    transform: translateY(-1px);
                }

                /* Empty state */
                .empty-state {
                    text-align: center;
                    padding: 64px 24px;
                    color: #475569;
                }

                .empty-state .icon {
                    font-size: 48px;
                    margin-bottom: 14px;
                }

                .empty-state p {
                    font-size: 15px;
                }

                /* Pagination */
                .pagination {
                    display: flex;
                    gap: 8px;
                    justify-content: flex-end;
                    margin-top: 24px;
                }

                .pagination button {
                    background: #14182b;
                    border: 1px solid #1f2538;
                    color: #94a3b8;
                    padding: 7px 14px;
                    border-radius: 8px;
                    cursor: pointer;
                    font-size: 13px;
                    transition: .15s;
                }

                .pagination button.active,
                .pagination button:hover {
                    background: #4f6cff;
                    color: #fff;
                    border-color: #4f6cff;
                }
            </style>
        </head>

        <body>
            <div class="app">
                <jsp:include page="./layout/sidebar.jsp" />

                <div class="content-errors">
                    <div class="errors-wrap">

                        <!-- Page heading -->
                        <div class="page-heading">
                            <h1>Error Reports</h1>
                            <p>Các báo cáo lỗi phần mềm do Reviewer xác nhận, đang chờ quyết định phê duyệt</p>
                        </div>

                        <!-- Stats strip -->
                        <div class="stats-strip">
                            <div class="stat-chip danger">
                                <div class="num">${empty errorReports ? 0 : errorReports.size()}</div>
                                <div class="lbl">Chờ xét duyệt</div>
                            </div>
                            <div class="stat-chip warn">
                                <div class="num">
                                    <c:set var="cntBug" value="0" />
                                    <c:forEach var="r" items="${errorReports}">
                                        <c:if test="${r.bugConfirmed == true}">
                                            <c:set var="cntBug" value="${cntBug + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    ${cntBug}
                                </div>
                                <div class="lbl">Lỗi đã xác nhận</div>
                            </div>
                        </div>

                        <!-- Filter bar -->
                        <div class="filter-bar">
                            <input type="text" id="fSoftware" placeholder="🔍 Tìm tên phần mềm...">
                            <input type="text" id="fReporter" placeholder="👤 Tìm người báo cáo...">
                            <input type="date" id="fDate" title="Lọc theo ngày báo cáo">
                        </div>

                        <!-- Table -->
                        <c:choose>
                            <c:when test="${empty errorReports}">
                                <div class="err-table-wrap">
                                    <div class="empty-state">
                                        <div class="icon">✅</div>
                                        <p>Không có báo cáo lỗi nào đang chờ xét duyệt.</p>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="err-table-wrap">
                                    <table class="err-table" id="errTable">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Phần mềm</th>
                                                <th>Người báo cáo</th>
                                                <th>Lý do báo cáo</th>
                                                <th>Reviewer xác nhận</th>
                                                <th>Ngày báo cáo</th>
                                                <th></th>
                                            </tr>
                                        </thead>
                                        <tbody id="tbody">
                                            <c:forEach var="r" items="${errorReports}" varStatus="st">
                                                <tr class="err-row" data-software="${r.softwareName}"
                                                    data-reporter="${r.reporterName}" data-date="${r.createdAt}">
                                                    <td class="date-col">${st.count}</td>
                                                    <td class="app-col">
                                                        <c:out value="${r.softwareName}" />
                                                    </td>
                                                    <td>
                                                        <c:out value="${r.reporterName}" />
                                                    </td>
                                                    <td class="reason-col" title="${r.reason}">
                                                        <c:out value="${r.reason}" />
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${r.bugConfirmed == true}">
                                                                <span class="badge-bug confirmed">⚠ Có lỗi</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge-bug unknown">— Chưa rõ</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="date-col">
                                                        <c:out value="${r.createdAt}" />
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/ApprovalErrorReportDetail?reportId=${r.reportId}"
                                                            class="btn-decide">Xét duyệt →</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="pagination" id="pager"></div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <script>
                const PER_PAGE = 8;
                const rows = Array.from(document.querySelectorAll('.err-row'));
                const pager = document.getElementById('pager');

                const fSoftware = document.getElementById('fSoftware');
                const fReporter = document.getElementById('fReporter');
                const fDate = document.getElementById('fDate');

                function filtered() {
                    const sw = (fSoftware?.value || '').toLowerCase();
                    const rp = (fReporter?.value || '').toLowerCase();
                    const dt = fDate?.value || ''; // YYYY-MM-DD from date input
                    return rows.filter(r => {
                        const nm = (r.dataset.software || '').toLowerCase();
                        const rn = (r.dataset.reporter  || '').toLowerCase();
                        // data-date may be "2026-03-26T18:02:25" or "2026-03-26 18:02:25"
                        const da = (r.dataset.date || '').substring(0, 10); // take YYYY-MM-DD part
                        return nm.startsWith(sw) && rn.startsWith(rp) && (!dt || da === dt);
                    });
                }

                function showPage(page, list) {
                    rows.forEach(r => r.style.display = 'none');
                    list.slice((page - 1) * PER_PAGE, page * PER_PAGE).forEach(r => r.style.display = '');
                }

                function buildPager(list) {
                    if (!pager) return;
                    pager.innerHTML = '';
                    const total = Math.ceil(list.length / PER_PAGE);
                    for (let i = 1; i <= total; i++) {
                        const b = document.createElement('button');
                        b.textContent = i;
                        b.onclick = () => {
                            showPage(i, list);
                            pager.querySelectorAll('button').forEach(x => x.classList.remove('active'));
                            b.classList.add('active');
                        };
                        pager.appendChild(b);
                    }
                    if (total > 0) pager.querySelector('button').click();
                }

                function refresh() {
                    const list = filtered();
                    rows.forEach(r => r.style.display = 'none');
                    if (!list.length) { if (pager) pager.innerHTML = ''; return; }
                    buildPager(list);
                }

                fSoftware?.addEventListener('input', refresh);
                fReporter?.addEventListener('input', refresh);
                fDate?.addEventListener('change', refresh);
                refresh();
            </script>
        </body>

        </html>