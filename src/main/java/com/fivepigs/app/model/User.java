package com.fivepigs.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private Integer userId;
    private String fullName;
    private String email;
    private String password;
    private Integer roleId;
    private String status; // ACTIVE, BLOCKED
    private LocalDateTime createdAt;
    private String roleName;
    private String avatar;

    public User() {
    }

    public User(String fullName, String email, String password, Integer roleId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.status = "ACTIVE"; // Mặc định ACTIVE khi đăng ký
    }

    public User(Integer userId, String fullName, String email, String password, Integer roleId, String status,
            LocalDateTime createdAt, String avatar) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
        this.createdAt = createdAt;
        this.avatar = avatar;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
