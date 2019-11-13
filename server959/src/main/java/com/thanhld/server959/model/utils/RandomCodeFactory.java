package com.thanhld.server959.model.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomCodeFactory {
    public static String getRandomCode(int quantity) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[quantity-1];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
}
