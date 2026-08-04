package com.avio.config;

import java.util.Base64;

public class Base64Example {

    public static String encode(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    public static String decode(String encodedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes);
    }

  
}