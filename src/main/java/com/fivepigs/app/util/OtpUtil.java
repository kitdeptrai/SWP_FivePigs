package com.fivepigs.app.util;

public class OtpUtil {
    public static String generateOtp() {
        return String.valueOf(100000 + new java.security.SecureRandom().nextInt(900000));
    }
}


