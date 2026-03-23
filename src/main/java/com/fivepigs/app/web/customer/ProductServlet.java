package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.ReviewDao;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Review;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.model.SoftwareDetail;
import com.fivepigs.app.model.SoftwareImage;
import com.fivepigs.app.model.SoftwarePricing;
import com.fivepigs.app.model.SoftwareVersion;
import com.fivepigs.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductServlet extends HttpServlet {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
    private final SoftwareDao softwareDao = new SoftwareDao();
    private final ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer softwareId = parseInt(request.getParameter("pid"));
        if (softwareId == null) {
            response.sendRedirect(request.getContextPath() + "/customer_dashboard");
            return;
        }

        try {
            Software detail = softwareDao.getSoftwareDetailBySoftwareId(softwareId);
            if (detail == null) {
                response.sendRedirect(request.getContextPath() + "/customer_dashboard");
                return;
            }

            SoftwareImage icon = softwareDao.getThumbnailBySoftwareId(String.valueOf(softwareId));
            List<SoftwareImage> screenshots = softwareDao.getScreenshotsBySoftwareId(String.valueOf(softwareId));
            List<Review> reviews = reviewDao.getReviewListBySoftwareId(softwareId);
            Map<Integer, Integer> ratingBreakdown = reviewDao.getRatingBreakdown(softwareId);
            List<SoftwarePricing> pricingOptions = softwareDao.getActivePricingBySoftwareId(softwareId);
            SoftwarePricing demoPricing = softwareDao.getDemoPricingBySoftwareId(softwareId);

            HttpSession session = request.getSession(false);
            User user = session == null ? null : (User) session.getAttribute("user");
            boolean canReview = false;
            boolean alreadyReviewed = false;
            if (user != null && user.getUserId() != null) {
                canReview = reviewDao.hasOwnedLicense(user.getUserId(), softwareId);
                alreadyReviewed = reviewDao.hasUserReviewed(user.getUserId(), softwareId);
            }

            request.setAttribute("detail", detail);
            request.setAttribute("icon", icon);
            request.setAttribute("screenshots", screenshots);
            request.setAttribute("reviews", reviews);
            request.setAttribute("reviewCount", reviews.size());
            request.setAttribute("ratingBreakdown", ratingBreakdown);
            request.setAttribute("canReview", canReview);
            request.setAttribute("alreadyReviewed", alreadyReviewed);
            request.setAttribute("reviewMsg", request.getParameter("reviewMsg"));
            request.setAttribute("demoMsg", request.getParameter("demoMsg"));
            request.setAttribute("pricingOptions", pricingOptions);
            request.setAttribute("defaultPricingId", pricingOptions.isEmpty() ? null : pricingOptions.get(0).getPricingId());
            request.setAttribute("demoPricing", demoPricing);
            request.setAttribute("hasDemoPlan", demoPricing != null && demoPricing.getPricingId() != null);
            request.setAttribute("avgRatingLabel", formatRating(detail.getAvgRating()));
            request.setAttribute("downloadCountLabel", formatDownloadCount(detail.getDownloadCount()));
            request.setAttribute("updateDateLabel", formatDate(resolveUpdateDate(detail)));
            request.setAttribute("fileSizeLabel", formatFileSize(resolveFileSize(detail)));
            request.setAttribute("featureLines", splitFeatureLines(detail));
            request.getRequestDispatcher("/WEB-INF/views/customer/single-product.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load product detail", e);
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime resolveUpdateDate(Software detail) {
        SoftwareVersion version = detail.getSoftwareVersion();
        if (version != null && version.getCreatedAt() != null) {
            return version.getCreatedAt();
        }
        return detail.getCreatedAt();
    }

    private Integer resolveFileSize(Software detail) {
        SoftwareVersion version = detail.getSoftwareVersion();
        return version == null ? null : version.getFileSize();
    }

    private String formatRating(Double rating) {
        if (rating == null || rating <= 0) {
            return "No rating yet";
        }
        return new DecimalFormat("0.0").format(rating);
    }

    private String formatDownloadCount(Integer downloadCount) {
        if (downloadCount == null || downloadCount <= 0) {
            return "New";
        }
        if (downloadCount >= 1_000_000) {
            return (downloadCount / 1_000_000) + "M+";
        }
        if (downloadCount >= 1_000) {
            return (downloadCount / 1_000) + "K+";
        }
        return downloadCount.toString();
    }

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Updating";
        }
        return dateTime.format(DATE_FORMAT);
    }

    private String formatFileSize(Integer fileSize) {
        if (fileSize == null || fileSize <= 0) {
            return "Updating";
        }
        double value = fileSize;
        String[] units = {"B", "KB", "MB", "GB"};
        int index = 0;
        while (value >= 1024 && index < units.length - 1) {
            value /= 1024;
            index++;
        }
        return new DecimalFormat(value >= 10 ? "0" : "0.0").format(value) + " " + units[index];
    }

    private List<String> splitFeatureLines(Software detail) {
        SoftwareDetail softwareDetail = detail.getSoftwareDetail();
        if (softwareDetail == null) {
            return Collections.emptyList();
        }

        String raw = softwareDetail.getReleaseNote();
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(raw.split("\\r?\\n|;|\\|"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
