package se.sprinta.headhunterbackend.system.exception;

/**
 * The user can request different file types for the ads. If the response doesn't live up to certain conditions, these exceptions are thrown.
 */
public class ResponseSubstringNotPureHtmlException extends RuntimeException {

    /**
     * Html format criteria is not met.
     * @param format This is the file format that the user asked for.
     */
    public ResponseSubstringNotPureHtmlException(String format) {
        super("Invalid string " + format + " response");
    }
}

