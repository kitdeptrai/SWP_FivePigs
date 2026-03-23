package com.fivepigs.app.web.customer;

import com.fivepigs.app.dao.UserDao;
import com.fivepigs.app.model.User;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.UUID;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 8
)
public class ProfileServlet extends HttpServlet {

    private static final String AVATAR_UPLOAD_DIR = "assets/uploads/avatars";
    private static final Set<String> ALLOWED_GENDERS = Set.of("Male", "Female", "Other", "Prefer not to say");
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User sessionUser = resolveSessionUser(request.getSession(false));
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/profile");
            return;
        }

        try {
            User latest = userDao.findByEmail(sessionUser.getEmail());
            if (latest != null) {
                request.getSession().setAttribute("user", latest);
                request.setAttribute("profileUser", latest);
            } else {
                request.setAttribute("profileUser", sessionUser);
            }

            request.setAttribute("activePage", "profile");
            request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User sessionUser = resolveSessionUser(request.getSession(false));
        if (sessionUser == null || sessionUser.getEmail() == null || sessionUser.getEmail().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/profile");
            return;
        }

        String fullName = trim(request.getParameter("fullName"));
        String phone = trim(request.getParameter("phone"));
        String dateOfBirthRaw = trim(request.getParameter("dateOfBirth"));
        String gender = trim(request.getParameter("gender"));
        String address = trim(request.getParameter("address"));
        String bio = trim(request.getParameter("bio"));

        if (fullName == null || fullName.length() < 2 || fullName.length() > 100) {
            response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_name");
            return;
        }
        if (phone != null && !phone.matches("[0-9+\\-\\s]{8,20}")) {
            response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_phone");
            return;
        }
        if (gender != null && !ALLOWED_GENDERS.contains(gender)) {
            response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_gender");
            return;
        }
        if (address != null && address.length() > 255) {
            response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_address");
            return;
        }
        if (bio != null && bio.length() > 1000) {
            response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_bio");
            return;
        }

        LocalDate dateOfBirth = null;
        if (dateOfBirthRaw != null) {
            try {
                dateOfBirth = LocalDate.parse(dateOfBirthRaw);
            } catch (DateTimeParseException e) {
                response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_dob");
                return;
            }
        }

        try {
            User latest = userDao.findByEmail(sessionUser.getEmail());
            if (latest == null) {
                response.sendRedirect(request.getContextPath() + "/profile?msg=update_failed");
                return;
            }

            String avatarPath = latest.getAvatar();
            Part avatarPart = request.getPart("avatarFile");
            if (avatarPart != null && avatarPart.getSize() > 0) {
                String saved = saveAvatar(request, avatarPart, latest.getUserId());
                if (saved == null) {
                    response.sendRedirect(request.getContextPath() + "/profile?msg=invalid_avatar");
                    return;
                }
                avatarPath = saved;
            }

           

            User refreshed = userDao.findByEmail(sessionUser.getEmail());
            if (refreshed != null) {
                request.getSession().setAttribute("user", refreshed);
            }
            response.sendRedirect(request.getContextPath() + "/profile?msg=updated");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String saveAvatar(HttpServletRequest request, Part avatarPart, int userId) throws IOException {
        String fileName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
        if (fileName.isBlank()) {
            return null;
        }

        String extension = "";
        int dot = fileName.lastIndexOf('.');
        if (dot >= 0) {
            extension = fileName.substring(dot).toLowerCase();
        }
        if (!(".png".equals(extension) || ".jpg".equals(extension) || ".jpeg".equals(extension) || ".webp".equals(extension))) {
            return null;
        }

        String uploadRoot = request.getServletContext().getRealPath("") == null
                ? null
                : new File(request.getServletContext().getRealPath("/")).getAbsolutePath();
        if (uploadRoot == null) {
            uploadRoot = new File("src/main/webapp").getAbsolutePath();
        }

        File uploadDir = new File(uploadRoot, AVATAR_UPLOAD_DIR);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new IOException("Cannot create avatar upload directory");
        }

        String storedFileName = "user_" + userId + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
        Path destination = uploadDir.toPath().resolve(storedFileName);
        Files.copy(avatarPart.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return "uploads/avatars/" + storedFileName;
    }

    private User resolveSessionUser(HttpSession session) {
        if (session == null) return null;
        return (User) session.getAttribute("user");
    }

    private String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
