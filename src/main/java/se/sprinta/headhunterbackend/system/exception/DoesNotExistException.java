package se.sprinta.headhunterbackend.system.exception;

public class DoesNotExistException extends RuntimeException {

    public DoesNotExistException() {
        super("Does not exist");
    }
}
