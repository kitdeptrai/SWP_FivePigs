package com.fivepigs.app.util;

import java.security.SecureRandom;

public class OtpUtil {
    public static String generateOtp() {
        return String.valueOf(100000 + new java.security.SecureRandom().nextInt(900000));
    }
}


