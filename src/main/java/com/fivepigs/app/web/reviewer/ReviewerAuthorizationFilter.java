package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = {
        "/reviewer_dashboard",
        "/reviewer_dashboard_detail",
        "/reviewer_pending",
        "/review_software",
        "/reviewer_error_reports",
        "/reviewer_error_report_detail",
        "/reviewer_guidelines",
        "/reviewer_guideline_create",
        "/reviewer_guideline_update",
        "/reviewer_guideline_delete",
        "/reviewer_guideline_items",
        "/reviewer_history",
        "/reviewer_history_detail",
        "/reviewer_notifications",
        "/reviewer_notification_toggle",
        "/reviewer_notification_delete",
        "/reviewer_notification_mark_all_read",
        "/reviewer_notification_delete_all",
        "/reviewer_profile",
        "/reviewer/report-download",
        "/reviewer/software-download"
})
public class ReviewerAuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest httpRequest)
                || !(response instanceof HttpServletResponse httpResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            String uri = httpRequest.getRequestURI();
            String contextPath = httpRequest.getContextPath();
            String target = uri.startsWith(contextPath) ? uri.substring(contextPath.length()) : uri;
            String query = httpRequest.getQueryString();
            if (query != null && !query.isBlank()) {
                target = target + "?" + query;
            }

            httpResponse.sendRedirect(contextPath + "/login?redirect="
                    + URLEncoder.encode(target, StandardCharsets.UTF_8));
            return;
        }

        if (!isReviewer(user, session)) {
            String roleName = resolveRoleName(user, session);
            httpResponse.sendRedirect(httpRequest.getContextPath() + resolveDashboardPath(roleName));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isReviewer(User user, HttpSession session) {
        String roleName = resolveRoleName(user, session);
        return roleName != null && "reviewer".equalsIgnoreCase(roleName.trim());
    }

    private String resolveRoleName(User user, HttpSession session) {
        if (user == null) {
            return null;
        }

        String roleName = user.getRoleName();
        if (roleName == null && session != null) {
            Object roleInSession = session.getAttribute("roleName");
            if (roleInSession instanceof String) {
                roleName = (String) roleInSession;
            }
        }
        return roleName;
    }

    private String resolveDashboardPath(String roleName) {
        if (roleName == null) {
            return "/customer_dashboard";
        }

        String role = roleName.trim().toLowerCase();
        return switch (role) {
            case "admin" -> "/admin/dashboard";
            case "aproval", "approval" -> "/approval_dashboard";
            case "reviewer" -> "/reviewer_dashboard";
            case "vendor" -> "/vendor/dashboard";
            case "customer", "user" -> "/customer_dashboard";
            default -> "/customer_dashboard";
        };
    }
}
