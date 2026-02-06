package com.watech.starwars.util;

public final class AuthUtil {

  private AuthUtil() {}

  public static String extractTokenFromAuthHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

    return authHeader.substring(7).trim();
  }
}
