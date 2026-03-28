package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.dao.UserDao;
import com.fivepigs.app.model.User;
import com.fivepigs.app.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/reviewer_profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 8 * 1024 * 1024
)
public class ReviewerProfileServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User sessionUser = session == null ? null : (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User latestUser = userDao.findByEmail(sessionUser.getEmail());
            if (latestUser != null) {
                session.setAttribute("user", latestUser);
                request.setAttribute("profileUser", latestUser);
            } else {
                request.setAttribute("profileUser", sessionUser);
            }

            request.setAttribute("activeMenu", "profile");
            request.getRequestDispatcher("/WEB-INF/views/reviewer/reviewer_profile.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User sessionUser = session == null ? null : (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email"));
        String currentPassword = trim(request.getParameter("currentPassword"));
        String newPassword = trim(request.getParameter("newPassword"));
        String confirmPassword = trim(request.getParameter("confirmPassword"));

        if (fullName == null || fullName.length() < 2) {
            response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=invalid_name");
            return;
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=invalid_email");
            return;
        }

        try {
            User latestUser = userDao.findByEmail(sessionUser.getEmail());
            if (latestUser == null) {
                response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=not_found");
                return;
            }

            if (userDao.emailExistsExceptUserId(email, latestUser.getUserId())) {
                response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=email_exists");
                return;
            }

            String avatarPath = latestUser.getAvatar();
            Part avatarPart = request.getPart("avatarFile");

            if (avatarPart != null && avatarPart.getSize() > 0) {
                String savedAvatar = saveAvatar(request, avatarPart, latestUser.getUserId());
                if (savedAvatar == null) {
                    response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=invalid_avatar");
                    return;
                }
                avatarPath = savedAvatar;
            }

            userDao.updateReviewerProfile(latestUser.getUserId(), fullName, email, avatarPath);

            boolean wantsChangePassword
                    = notBlank(currentPassword) || notBlank(newPassword) || notBlank(confirmPassword);

            if (wantsChangePassword) {
                if (!notBlank(currentPassword) || !notBlank(newPassword) || !notBlank(confirmPassword)) {
                    response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=password_missing");
                    return;
                }

                String currentPasswordHash = PasswordUtil.sha256(currentPassword);
                if (!latestUser.getPassword().equals(currentPasswordHash)) {
                    response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=current_password_wrong");
                    return;
                }

                if (newPassword.length() < 6) {
                    response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=password_too_short");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=password_not_match");
                    return;
                }

                String newPasswordHash = PasswordUtil.sha256(newPassword);
                userDao.updatePasswordByUserId(latestUser.getUserId(), newPasswordHash);
            }

            User updatedUser = userDao.findByEmail(email);
            if (updatedUser != null) {
                session.setAttribute("user", updatedUser);
            }

            response.sendRedirect(request.getContextPath() + "/reviewer_profile?msg=updated");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String saveAvatar(HttpServletRequest request, Part avatarPart, int userId) throws IOException {
        String originalFileName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
        if (originalFileName == null || originalFileName.isBlank()) {
            return null;
        }

        String ext = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            ext = originalFileName.substring(dotIndex).toLowerCase();
        }

        String contentType = avatarPart.getContentType();
        boolean validExtension
                = ext.equals(".png") || ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".webp");

        boolean validContentType
                = "image/png".equals(contentType)
                || "image/jpeg".equals(contentType)
                || "image/webp".equals(contentType);

        if (!validExtension || !validContentType) {
            return null;
        }

        String uploadFolderPath = request.getServletContext().getRealPath("/assets/uploads/avatars");
        File uploadFolder = new File(uploadFolderPath);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String savedFileName = "reviewer_" + userId + "_" + UUID.randomUUID().toString().replace("-", "") + ext;
        Path savePath = uploadFolder.toPath().resolve(savedFileName);

        Files.copy(avatarPart.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/avatars/" + savedFileName;
    }

    private String trim(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
