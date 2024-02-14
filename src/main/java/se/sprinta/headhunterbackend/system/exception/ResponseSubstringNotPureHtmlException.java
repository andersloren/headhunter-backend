package se.sprinta.headhunterbackend.system.exception;

public class ResponseSubstringNotPureHtmlException extends RuntimeException {

    public ResponseSubstringNotPureHtmlException(String format) {
        super("Invalid string " + format + " response");
    }
}

