package com.watech.starwars.controller;

import com.watech.starwars.model.auth.LoginRequest;
import com.watech.starwars.model.auth.LoginResponse;
import com.watech.starwars.model.auth.RefreshRequest;
import com.watech.starwars.model.auth.RefreshResponse;
import com.watech.starwars.service.AuthService;
import com.watech.starwars.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public Mono<LoginResponse> login(@RequestBody LoginRequest r) {
    return Mono.fromSupplier(() -> authService.login(r.username(), r.password()));
  }

  @PostMapping("/refresh")
  public Mono<RefreshResponse> refresh(@RequestBody RefreshRequest r) {
    return Mono.fromSupplier(() -> authService.refresh(r.refreshToken()));
  }

  @PostMapping("/logout")
  public Mono<ResponseEntity<Void>> logout(
      @RequestHeader(value = "Authorization", required = false) String authHeader) {
    String accessToken = AuthUtil.extractTokenFromAuthHeader(authHeader);

    return Mono.fromRunnable(() -> authService.clearMapsByAccessToken(accessToken))
        .thenReturn(ResponseEntity.noContent().build());
  }
}
