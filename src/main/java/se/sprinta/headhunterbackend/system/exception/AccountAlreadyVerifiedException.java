package se.sprinta.headhunterbackend.system.exception;

public class AccountAlreadyVerifiedException extends RuntimeException {

    public AccountAlreadyVerifiedException(String email) {
        super("Account is already verified");
    }
}
