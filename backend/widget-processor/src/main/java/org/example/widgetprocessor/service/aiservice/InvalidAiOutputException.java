package org.example.widgetprocessor.service.aiservice;

public class InvalidAiOutputException extends RuntimeException {

  public InvalidAiOutputException(String message) {
    super(message);
  }
  public InvalidAiOutputException(String message, Throwable cause) {
    super(message, cause);
  }
}
