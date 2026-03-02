package com.fivepigs.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OtpUtil {
    public static String generateOtp() {
        return String.valueOf(100000 + new java.security.SecureRandom().nextInt(900000));
    }

    public static String hashOtp(String otp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(otp.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}


