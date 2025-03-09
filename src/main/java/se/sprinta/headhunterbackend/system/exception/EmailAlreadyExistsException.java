package se.sprinta.headhunterbackend.system.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email is already registered");
    }
}
