package se.sprinta.headhunterbackend.system.exception;

public class DoesNotExistException extends RuntimeException {

    /**
     * When there is a mismatch between owning side and inverse side, DoesNotExistException is being thrown.
     * As there might be malicious intents behind such an event, the user will not be told which of them, or both of them, are wrong.
     */

    public DoesNotExistException() {
        super("Does not exist");
    }
}
