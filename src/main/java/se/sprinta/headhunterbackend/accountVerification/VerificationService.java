package se.sprinta.headhunterbackend.accountVerification;

import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.email.MicrosoftGraphAuth;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;

    private final MicrosoftGraphAuth microsoftGraphAuth;

    public VerificationService(VerificationRepository verificationRepository, MicrosoftGraphAuth microsoftGraphAuth) {
        this.verificationRepository = verificationRepository;
        this.microsoftGraphAuth = microsoftGraphAuth;
    }

    public String createVerification(Account account) {
        Verification verification = new Verification();
        verification.setAccount(account);
        Verification savedVerification = this.verificationRepository.save(verification);
        return savedVerification.getVerificationCode();
    }

    public void sendVerificationEmail(Account account) throws IOException, URISyntaxException {
        String verificationCode = createVerification(account);
        this.microsoftGraphAuth.sendVerificationEmail(account.getEmail(), verificationCode);
    }
}

