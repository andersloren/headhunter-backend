package se.sprinta.headhunterbackend.system.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, String email) {
        super("Could not find " + objectName + " with Email " + email);
    }
}
