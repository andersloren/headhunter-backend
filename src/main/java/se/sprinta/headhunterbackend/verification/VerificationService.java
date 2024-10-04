package se.sprinta.headhunterbackend.verification;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.email.MicrosoftGraphAuth;
import se.sprinta.headhunterbackend.system.exception.AccountAlreadyVerifiedException;
import se.sprinta.headhunterbackend.system.exception.InvalidVerificationCodeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Transactional
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final AccountRepository accountRepository;
    private final MicrosoftGraphAuth microsoftGraphAuth;

    public VerificationService(VerificationRepository verificationRepository, AccountRepository accountRepository, MicrosoftGraphAuth microsoftGraphAuth) {
        this.verificationRepository = verificationRepository;
        this.accountRepository = accountRepository;
        this.microsoftGraphAuth = microsoftGraphAuth;
    }

    public List<Verification> findAll() {
        return this.verificationRepository.findAll();
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

    public void verifyRegistration(String email, String verificationCode) throws IOException, URISyntaxException {
        Account foundAccount = this.accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("account", email));

        String foundVerificationCode = this.verificationRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("verification code", email));

        // TODO: 10/3/2024 Create test for this if statement
        if (foundAccount.isVerified()) {
            throw new AccountAlreadyVerifiedException(email);
        }

        // TODO: 10/3/2024 Create test for this if statement
        if (!foundVerificationCode.equals(verificationCode)) {
            sendVerificationEmail(foundAccount);
            throw new InvalidVerificationCodeException(email, verificationCode);
        }

        foundAccount.setVerified(true);
        this.verificationRepository.deleteByEmail(email);
    }
}

