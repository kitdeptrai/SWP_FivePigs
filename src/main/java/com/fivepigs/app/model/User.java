package com.fivepigs.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private Integer userId;
    private String fullName;
    private String email;
    private String password;
    private Integer roleId;
    private String avatar;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String bio;
    private String status;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String fullName, String email, String password, Integer roleId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.status = "ACTIVE";
    }

    public User(Integer userId, String fullName, String email, String password, Integer roleId, String avatar,
                String phone, LocalDate dateOfBirth, String gender, String address, String bio, String status, LocalDateTime createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.avatar = avatar;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.bio = bio;
        this.status = status;
        this.createdAt = createdAt;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
}
