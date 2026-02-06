package com.watech.starwars.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements WebFilter {

  private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    ServerHttpRequest req = exchange.getRequest();

    long start = System.currentTimeMillis();

    return chain
        .filter(exchange)
        .doOnSuccess(
            e -> {
              long duration = System.currentTimeMillis() - start;

              HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
              int statusValue = statusCode != null ? statusCode.value() : 200;

              log.info("{} {} -> {} ({}ms)", req.getMethod(), req.getURI(), statusValue, duration);
            });
  }
}
