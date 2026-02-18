package com.fivepigs.app.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {
    private PasswordUtil() {}

    public static String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // ===== BCrypt (khuyến nghị) =====
    // Cần dependency: org.mindrot:jbcrypt:0.4
    //
    // public static String bcryptHash(String raw) {
    //     return org.mindrot.jbcrypt.BCrypt.hashpw(raw, org.mindrot.jbcrypt.BCrypt.gensalt(12));
    // }
    //
    // public static boolean bcryptCheck(String raw, String hashed) {
    //     if (raw == null || hashed == null) return false;
    //     return org.mindrot.jbcrypt.BCrypt.checkpw(raw, hashed);
    // }
}
