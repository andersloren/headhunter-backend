package se.sprinta.headhunterbackend.system.exception;

/**
 * Common exception that is suitable for customization to our needs.
 *
 * The method overload is due to different data types for the id of the models.
 */

public class ObjectNotFoundException extends RuntimeException {

    /**
     * For Ad objects
     * @param objectName This is the type of object that was not found. In the current situation, this should always be 'ad'.
     * @param id The id the of the object not found.
     */

    public ObjectNotFoundException(String objectName, String id) {
        super(objectName == "user" ? "Could not find " + objectName + " with Email " + id : "Could not find " + objectName + " with Id " + id);
    }

    /**
     /**
     * For User and Job objects
     * @param objectName This is the type of object that was not found.
     * @param id The id the of the object not found.
     */

    public ObjectNotFoundException(String objectName, Long id) {
        super("Could not find " + objectName + " with Id " + id);
    }
}
