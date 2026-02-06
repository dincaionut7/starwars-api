package com.watech.starwars.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class AuthUtil {

  private static final SecureRandom secureRandom = new SecureRandom();

  private AuthUtil() {}

  public static String extractTokenFromAuthHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

    return authHeader.substring(7).trim();
  }

  public static String generateToken() {
    byte[] bytes = new byte[32];
    secureRandom.nextBytes(bytes);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
