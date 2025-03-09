package se.sprinta.headhunterbackend.system.exception;

public class AccountAlreadyExistException extends RuntimeException {
  public AccountAlreadyExistException(String message) {
    super(message);
  }
}
