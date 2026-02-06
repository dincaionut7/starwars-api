package com.watech.starwars.filter;

import com.watech.starwars.service.AuthService;
import com.watech.starwars.util.AuthUtil;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements WebFilter {

  private static final Set<String> PROTECTED = Set.of("/favourites");

  private final AuthService authService;

  public AuthFilter(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    String path = exchange.getRequest().getPath().value();
    HttpHeaders headers = exchange.getRequest().getHeaders();

    if (!PROTECTED.contains(path)) return chain.filter(exchange);

    String authHeader = headers.getFirst("Authorization");

    String accessToken = AuthUtil.extractTokenFromAuthHeader(authHeader);

    if (authService.isAccessTokenValid(accessToken)) return chain.filter(exchange);

    // unauthorized, try refresh

    String refreshToken = headers.getFirst("Refresh-Token");

    String newAccesToken;

    try {
      newAccesToken = authService.refresh(refreshToken).newAccessToken();
      exchange.getResponse().getHeaders().set("New-Access-Token", newAccesToken);

      // mutate the request??

      return chain.filter(exchange);

    } catch (Exception e) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

      return exchange.getResponse().setComplete();
    }
  }
}
