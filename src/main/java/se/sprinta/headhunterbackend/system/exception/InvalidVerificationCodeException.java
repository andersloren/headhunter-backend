package se.sprinta.headhunterbackend.system.exception;

public class InvalidVerificationCodeException extends RuntimeException {

    public InvalidVerificationCodeException(String email, String verificationCode) {
        super(verificationCode + " send by " + email + " is invalid");
    }
}
