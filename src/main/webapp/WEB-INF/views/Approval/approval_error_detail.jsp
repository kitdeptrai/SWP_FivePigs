<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Chi tiết Báo cáo Lỗi – Approval</title>
            <link rel="stylesheet" href="/assets/css/Approval/approval.css">
            <style>
                /* ──────────────────────────────
           Layout
        ────────────────────────────── */
                .content-error-detail {
                    background: #0f1220;
                    min-height: 100vh;
                    padding: 44px 56px;
                    margin-left: 260px;
                    width: calc(100% - 260px);
                }

                .detail-wrap {
                    max-width: 900px;
                    margin: 0 auto;
                }

                /* ──────────────────────────────
           Top bar
        ────────────────────────────── */
                .top-bar {
                    display: flex;
                    justify-content: space-between;
                    align-items: flex-start;
                    margin-bottom: 36px;
                    gap: 16px;
                }

                .top-bar h1 {
                    font-size: 26px;
                    font-weight: 800;
                    color: #f3f6ff;
                }

                .top-bar .sub {
                    font-size: 13px;
                    color: #64748b;
                    margin-top: 5px;
                }

                .btn-back {
                    display: inline-block;
                    background: #14182b;
                    border: 1px solid #1f2538;
                    padding: 10px 18px;
                    border-radius: 10px;
                    color: #94a3b8;
                    font-weight: 600;
                    font-size: 13px;
                    text-decoration: none;
                    transition: .2s;
                    white-space: nowrap;
                }

                .btn-back:hover {
                    border-color: #4f6cff;
                    color: #fff;
                }

                /* ──────────────────────────────
           Section card
        ────────────────────────────── */
                .section-card {
                    background: #14182b;
                    border: 1px solid #1f2538;
                    border-radius: 16px;
                    padding: 26px;
                    margin-bottom: 22px;
                }

                .section-title {
                    font-size: 11px;
                    font-weight: 800;
                    color: #64748b;
                    text-transform: uppercase;
                    letter-spacing: .7px;
                    margin-bottom: 20px;
                }

                /* ──────────────────────────────
           Info grid
        ────────────────────────────── */
                .info-grid {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 18px 40px;
                }

                .info-field label {
                    display: block;
                    font-size: 11px;
                    font-weight: 700;
                    color: #475569;
                    text-transform: uppercase;
                    letter-spacing: .5px;
                    margin-bottom: 5px;
                }

                .info-field .val {
                    font-size: 14px;
                    color: #e2e8f0;
                }

                .info-field.full {
                    grid-column: 1 / -1;
                }

                /* bug badge */
                .badge-bug {
                    display: inline-flex;
                    align-items: center;
                    gap: 5px;
                    padding: 5px 14px;
                    border-radius: 999px;
                    font-size: 12px;
                    font-weight: 700;
                }

                .badge-bug.confirmed {
                    background: rgba(239, 68, 68, .14);
                    color: #ef4444;
                    border: 1px solid rgba(239, 68, 68, .35);
                }

                .badge-bug.unknown {
                    background: rgba(148, 163, 184, .08);
                    color: #94a3b8;
                    border: 1px solid rgba(148, 163, 184, .25);
                }

                /* Text block */
                .txt-block {
                    background: #0f1220;
                    border: 1px solid #1f2538;
                    border-radius: 10px;
                    padding: 14px 16px;
                    font-size: 13px;
                    color: #94a3b8;
                    line-height: 1.65;
                    white-space: pre-wrap;
                    word-break: break-word;
                    margin-top: 6px;
                }

                /* ──────────────────────────────
           Decision form
        ────────────────────────────── */
                .decision-row {
                    display: flex;
                    gap: 14px;
                    margin-bottom: 20px;
                }

                /* Hide the actual radio */
                .decision-row input[type="radio"] {
                    display: none;
                }

                .choice-card {
                    flex: 1;
                    border-radius: 14px;
                    border: 2px solid #1f2538;
                    background: #0f1220;
                    padding: 20px;
                    cursor: pointer;
                    transition: .2s;
                    display: flex;
                    flex-direction: column;
                    gap: 6px;
                }

                .choice-card:hover {
                    border-color: #4f6cff;
                }

                /* Approve option */
                #opt-approve:checked~.decision-row .approve-card {
                    border-color: #22c55e;
                    background: rgba(34, 197, 94, .08);
                }

                .approve-card .choice-icon {
                    color: #22c55e;
                    font-size: 22px;
                }

                .approve-card .choice-title {
                    font-weight: 700;
                    color: #22c55e;
                    font-size: 15px;
                }

                .approve-card .choice-desc {
                    font-size: 12px;
                    color: #64748b;
                    line-height: 1.5;
                }

                /* Reject option */
                #opt-reject:checked~.decision-row .reject-card {
                    border-color: #ef4444;
                    background: rgba(239, 68, 68, .08);
                }

                .reject-card .choice-icon {
                    color: #ef4444;
                    font-size: 22px;
                }

                .reject-card .choice-title {
                    font-weight: 700;
                    color: #ef4444;
                    font-size: 15px;
                }

                .reject-card .choice-desc {
                    font-size: 12px;
                    color: #64748b;
                    line-height: 1.5;
                }

                /* Use JS to show selected state since CSS sibling selector won't work for hidden radio inside flex */
                .choice-card.selected-approve {
                    border-color: #22c55e !important;
                    background: rgba(34, 197, 94, .08) !important;
                }

                .choice-card.selected-reject {
                    border-color: #ef4444 !important;
                    background: rgba(239, 68, 68, .08) !important;
                }

                /* Note textarea */
                .field-label {
                    font-size: 11px;
                    font-weight: 700;
                    color: #475569;
                    text-transform: uppercase;
                    letter-spacing: .5px;
                    display: block;
                    margin-bottom: 8px;
                }

                textarea.note-area {
                    width: 100%;
                    background: #0f1220;
                    border: 1px solid #1f2538;
                    border-radius: 10px;
                    color: #e2e8f0;
                    font-size: 13px;
                    padding: 14px;
                    resize: vertical;
                    outline: none;
                    font-family: inherit;
                    transition: border-color .2s;
                    min-height: 90px;
                }

                textarea.note-area:focus {
                    border-color: #4f6cff;
                }

                textarea.note-area::placeholder {
                    color: #475569;
                }

                /* Validation warning */
                .warn-msg {
                    display: none;
                    font-size: 12px;
                    color: #ef4444;
                    margin-bottom: 14px;
                }

                /* Action row */
                .action-row {
                    display: flex;
                    justify-content: flex-end;
                    gap: 12px;
                    margin-top: 22px;
                }

                .btn-cancel {
                    background: #14182b;
                    border: 1px solid #1f2538;
                    padding: 11px 24px;
                    border-radius: 10px;
                    color: #94a3b8;
                    font-weight: 600;
                    font-size: 14px;
                    text-decoration: none;
                    cursor: pointer;
                    transition: .2s;
                }

                .btn-cancel:hover {
                    border-color: #4f6cff;
                    color: #fff;
                }

                .btn-submit {
                    background: linear-gradient(135deg, #4f6cff, #3b50d9);
                    border: none;
                    padding: 11px 30px;
                    border-radius: 10px;
                    color: #fff;
                    font-weight: 700;
                    font-size: 14px;
                    cursor: pointer;
                    transition: .2s;
                }

                .btn-submit:hover {
                    box-shadow: 0 6px 18px rgba(79, 108, 255, .4);
                    transform: translateY(-1px);
                }

                /* Empty reviewer note */
                .no-data {
                    color: #475569;
                    font-size: 13px;
                    font-style: italic;
                }
            </style>
        </head>

        <body>
            <div class="app">
                <jsp:include page="./layout/sidebar.jsp" />

                <div class="content-error-detail">
                    <div class="detail-wrap">

                        <!-- Top bar -->
                        <div class="top-bar">
                            <div>
                                <h1>Chi tiết Báo cáo Lỗi</h1>
                                <div class="sub">Xem xét báo cáo và đưa ra quyết định cuối cùng</div>
                            </div>
                            <a href="${pageContext.request.contextPath}/ApprovalErrorReports?page=error_reports"
                                class="btn-back">← Quay lại</a>
                        </div>

                        <!-- Section 1: Report info -->
                        <div class="section-card">
                            <div class="section-title">📋 Thông tin báo cáo</div>
                            <div class="info-grid">
                                <div class="info-field">
                                    <label>Phần mềm</label>
                                    <div class="val">
                                        <c:out value="${report.softwareName}" />
                                    </div>
                                </div>
                                <div class="info-field">
                                    <label>Người báo cáo</label>
                                    <div class="val">
                                        <c:out value="${report.reporterName}" />
                                    </div>
                                </div>
                                <div class="info-field">
                                    <label>Ngày báo cáo</label>
                                    <div class="val">
                                        <c:out value="${report.createdAt}" />
                                    </div>
                                </div>
                                <div class="info-field">
                                    <label>Reviewer xác nhận lỗi</label>
                                    <div class="val">
                                        <c:choose>
                                            <c:when test="${report.bugConfirmed == true}">
                                                <span class="badge-bug confirmed">⚠ Có lỗi</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-bug unknown">— Không xác nhận</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="info-field full">
                                    <label>Lý do báo cáo từ người dùng</label>
                                    <div class="txt-block">
                                        <c:out value="${report.reason}" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Section 2: Reviewer findings -->
                        <div class="section-card">
                            <div class="section-title">🔬 Kết quả kiểm tra từ Reviewer</div>
                            <c:choose>
                                <c:when test="${not empty report.reproduceSteps or not empty report.reviewerNote}">
                                    <c:if test="${not empty report.reproduceSteps}">
                                        <label class="field-label">Các bước tái hiện lỗi</label>
                                        <div class="txt-block">
                                            <c:out value="${report.reproduceSteps}" />
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty report.reviewerNote}">
                                        <label class="field-label" style="margin-top:16px;">Ghi chú của Reviewer</label>
                                        <div class="txt-block" style="border-color:rgba(79,108,255,.3);">
                                            <c:out value="${report.reviewerNote}" />
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <p class="no-data">Reviewer không để lại ghi chú.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Section 3: Decision -->
                        <div class="section-card">
                            <div class="section-title">⚖️ Quyết định phê duyệt</div>

                            <form method="post" action="${pageContext.request.contextPath}/ApprovalErrorReportDetail"
                                id="decisionForm">
                                <input type="hidden" name="reportId" value="${report.reportId}">
                                <input type="hidden" name="decision" id="decisionInput">

                                <!-- Choice cards -->
                                <div class="decision-row" id="choiceRow">
                                    <!-- APPROVE -->
                                    <div class="choice-card approve-card" id="cardApprove"
                                        onclick="selectDecision('APPROVE')">
                                        <div class="choice-icon">✅</div>
                                        <div class="choice-title">APPROVE – Xác nhận lỗi</div>
                                    </div>
                                    <!-- REJECT -->
                                    <div class="choice-card reject-card" id="cardReject"
                                        onclick="selectDecision('REJECT')">
                                        <div class="choice-icon">✖</div>
                                        <div class="choice-title">REJECT – Bác bỏ báo cáo</div>
                                    </div>
                                </div>

                                <p class="warn-msg" id="warnMsg">⚠ Vui lòng chọn một quyết định trước khi gửi.</p>

                                <!-- Note -->
                                <label class="field-label">Ghi chú (tùy chọn)</label>
                                <textarea name="note" class="note-area"
                                    placeholder="Nhập lý do hoặc ghi chú bổ sung cho quyết định này..."></textarea>

                                <!-- Actions -->
                                <div class="action-row">
                                    <a href="${pageContext.request.contextPath}/ApprovalErrorReports?page=error_reports"
                                        class="btn-cancel">Hủy</a>
                                    <button type="submit" class="btn-submit">Gửi quyết định</button>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
            </div>

            <script>
                let chosen = null;

                function selectDecision(val) {
                    chosen = val;
                    document.getElementById('decisionInput').value = val;
                    document.getElementById('cardApprove').classList.remove('selected-approve');
                    document.getElementById('cardReject').classList.remove('selected-reject');
                    if (val === 'APPROVE') {
                        document.getElementById('cardApprove').classList.add('selected-approve');
                    } else {
                        document.getElementById('cardReject').classList.add('selected-reject');
                    }
                    document.getElementById('warnMsg').style.display = 'none';
                }

                document.getElementById('decisionForm').addEventListener('submit', function (e) {
                    if (!chosen) {
                        e.preventDefault();
                        document.getElementById('warnMsg').style.display = 'block';
                    }
                });
            </script>
        </body>

        </html>