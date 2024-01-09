package dev.manyroads.exceptions;

public class BadCredentialsException extends RuntimeException {
  public BadCredentialsException() {}

  public BadCredentialsException(String message) {
    super(message);
  }

  public BadCredentialsException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadCredentialsException(Throwable cause) {
    super(cause);
  }
}
