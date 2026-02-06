package com.watech.starwars.exception;

public class SwapiUnavailableException extends RuntimeException {

  public SwapiUnavailableException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
