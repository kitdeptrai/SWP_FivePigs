package com.fivepigs.app.service;

import com.fivepigs.app.dao.UserDao;
import com.fivepigs.app.model.User;
import com.fivepigs.app.util.PasswordUtil;

import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;
    private static final String DEFAULT_ROLE = "User"; // Role mặc định khi đăng ký

    public UserService() {
        this.userDao = new UserDao();
    }

    public boolean emailExists(String email) throws SQLException {
        return userDao.emailExists(email);
    }

    public User registerUser(String fullName, String email, String password) throws SQLException {
        // Hash password (TẠM THỜI VÔ HIỆU HÓA)
//         String passwordHash = PasswordUtil.sha256(password);
       String passwordHash = password; // Sử dụng mật khẩu gốc
        
        // Lấy role_id của CUSTOMER (role mặc định)
        Integer roleId = userDao.getRoleIdByName(DEFAULT_ROLE);
        if (roleId == null) {
            // Ném lỗi nếu không tìm thấy role mặc định trong DB.
            // Hãy chắc chắn rằng bảng Role có một dòng với role_name = 'User'.
            throw new SQLException("Default role '" + DEFAULT_ROLE + "' không tồn tại trong database.");
        }
        
        // Tạo user với role CUSTOMER và status ACTIVE
        User user = new User(fullName, email, passwordHash, roleId);
        userDao.insertUser(user);
        return user;
    }

    public String validateRegistration(String fullName, String email, String password, String confirmPassword) {
        if (fullName == null || fullName.trim().length() < 2 || fullName.trim().length() > 100) {
            return "Họ và tên không hợp lệ (2-100 ký tự)";
        }
        if (email == null || email.trim().length() < 5 || email.trim().length() > 100 || !email.contains("@")) {
            return "Email không hợp lệ (5-100 ký tự)";
        }
        if (password == null || password.length() < 6 || password.length() > 72) {
            return "Mật khẩu phải từ 6 đến 72 ký tự";
        }
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu nhập lại không khớp";
        }
        return null;
    }

    public User login(String email, String password) throws SQLException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return null; // Email không tồn tại
        }
        
        // Kiểm tra status
        if (!"ACTIVE".equals(user.getStatus())) {
            return null; // Tài khoản bị khóa
        }
        
//         Kiểm tra password (TẠM THỜI VÔ HIỆU HÓA MÃ HÓA)
//         String passwordHash = PasswordUtil.sha256(password);
//         if (!passwordHash.equals(user.getPassword())) {
//             return null; // Password sai
//         }

        // So sánh mật khẩu gốc (plain text)
        if (!password.equals(user.getPassword())) {
            return null; // Password sai
        }
        
        return user;
    }

    public String getRoleName(Integer roleId) throws SQLException {
        return userDao.getRoleNameById(roleId);
    }

    public void resetPassword(String email, String newPassword) throws SQLException {
        // Tạm thời vô hiệu hóa mã hóa, chỉ lưu mật khẩu gốc
//         String passwordHash = PasswordUtil.sha256(newPassword);
        String passwordHash = newPassword;
        userDao.updatePassword(email, passwordHash);
    }
}
