package com.example.newbst.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String generateHashWithSalt(String password) {
        String salt="SunJingHaHa";
        try {
            String saltedPassword = salt + password;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing error", e);
        }
    }

    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        String generatedHash = generateHashWithSalt(inputPassword);
        return generatedHash.equals(hashedPassword);
    }
}
