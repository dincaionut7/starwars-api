package com.watech.starwars.exception;

public class ExternalApiClientException extends RuntimeException {

  public ExternalApiClientException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
