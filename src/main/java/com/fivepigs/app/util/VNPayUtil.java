/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.util;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author thanh
 */

public class VNPayUtil {

    public static String hmacSHA512(String key, String data) {
        try {

            Mac hmac512 = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKey =
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

            hmac512.init(secretKey);

            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();

            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();

        } catch (Exception ex) {
            throw new RuntimeException("Error while hashing VNPay data", ex);
        }
    }
}

