package com.watech.starwars.service;

import com.watech.starwars.exception.BadRequestException;
import com.watech.starwars.exception.UnauthorizedException;
import com.watech.starwars.model.auth.LoginResponse;
import com.watech.starwars.model.auth.RefreshResponse;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private record Session(String username, String refreshToken) {}

  private final ConcurrentHashMap<String, Session> sessionsByAccessToken =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, String> accessTokensByRefreshToken =
      new ConcurrentHashMap<>();

  private final SecureRandom secureRandom = new SecureRandom();

  public LoginResponse login(String username, String password) {
    if (username == null || username.isBlank() || password == null || password.isBlank())
      throw new BadRequestException("Username and password required");

    // accept any

    String accessToken = generateToken();
    String refreshToken = generateToken();

    sessionsByAccessToken.put(accessToken, new Session(username, refreshToken));
    accessTokensByRefreshToken.put(refreshToken, accessToken);

    return new LoginResponse(accessToken, refreshToken, username);
  }

  public RefreshResponse refresh(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank())
      throw new BadRequestException("Refresh token is required");

    String oldAccessToken = accessTokensByRefreshToken.get(refreshToken);
    if (oldAccessToken == null) throw new UnauthorizedException("Invalid refresh token");

    Session oldSession = sessionsByAccessToken.remove(oldAccessToken);
    if (oldSession == null) throw new UnauthorizedException("Access Token expired");

    String newAccessToken = generateToken();

    sessionsByAccessToken.put(newAccessToken, new Session(oldSession.username(), refreshToken));
    accessTokensByRefreshToken.put(refreshToken, newAccessToken);

    return new RefreshResponse(newAccessToken);
  }

  public boolean isAccessTokenValid(String accessToken) {
    return accessToken != null && sessionsByAccessToken.containsKey(accessToken);
  }

  public void clearMapsByAccessToken(String accessToken) {
    if (accessToken == null || accessToken.isBlank()) return;

    Session session = sessionsByAccessToken.remove(accessToken);
    if (session != null) accessTokensByRefreshToken.remove(session.refreshToken());
  }

  private String generateToken() {
    byte[] bytes = new byte[32];
    secureRandom.nextBytes(bytes);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
