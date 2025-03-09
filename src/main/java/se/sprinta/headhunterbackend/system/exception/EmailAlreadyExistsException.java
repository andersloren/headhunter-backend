package se.sprinta.headhunterbackend.system.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException() {
        super("Email is already registered");
    }
}
