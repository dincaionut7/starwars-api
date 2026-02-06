package com.watech.starwars.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
