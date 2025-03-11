package se.sprinta.headhunterbackend.system.exception;

public class TokenDoesNotExistException extends RuntimeException {

    public TokenDoesNotExistException() {
        super("Token does not exist");
    }
}
