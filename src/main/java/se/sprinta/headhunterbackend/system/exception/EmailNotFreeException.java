package se.sprinta.headhunterbackend.system.exception;

public class EmailNotFreeException extends RuntimeException {

    public EmailNotFreeException(String email) {
        super(email + " is already registered");
    }
}