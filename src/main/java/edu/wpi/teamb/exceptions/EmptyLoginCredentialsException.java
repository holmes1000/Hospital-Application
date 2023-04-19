package edu.wpi.teamb.exceptions;

public class EmptyLoginCredentialsException extends Exception {
  public EmptyLoginCredentialsException(String message) {
    super(message);
  }
}
