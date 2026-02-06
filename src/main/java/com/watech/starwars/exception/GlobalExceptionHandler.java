package com.watech.starwars.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFound(NotFoundException e, ServerWebExchange exchange) {
    log.warn(
        "404 {} {} - {}",
        exchange.getRequest().getMethod(),
        exchange.getRequest().getURI(),
        e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(SwapiUnavailableException.class)
  public ResponseEntity<String> handleSwapiUnavailable(
      SwapiUnavailableException e, ServerWebExchange exchange) {
    log.error(
        "503 {} {} - {}",
        exchange.getRequest().getMethod(),
        exchange.getRequest().getURI(),
        e.getMessage());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
  }

  @ExceptionHandler(ExternalApiClientException.class)
  public ResponseEntity<String> handleExternal4xxError(
      ExternalApiClientException e, ServerWebExchange exchange) {
    log.error(
        "502 {} {} - {}",
        exchange.getRequest().getMethod(),
        exchange.getRequest().getURI(),
        e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<String> handleUnauthorized(
      UnauthorizedException e, ServerWebExchange exchange) {
    log.warn(
        "401 {} {} - {}",
        exchange.getRequest().getMethod(),
        exchange.getRequest().getURI(),
        e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequest(
      BadRequestException e, ServerWebExchange exchange) {
    log.error(
        "400 {} {} - {}",
        exchange.getRequest().getMethod(),
        exchange.getRequest().getURI(),
        e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }
}
