package se.sprinta.headhunterbackend.account;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.exception.EmailAlreadyExistsException;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.verification.VerificationService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Business logic for Job
 */

@Service
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    public AccountService(
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            VerificationService verificationService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
    }

    public List<Account> findAll() {
        return this.accountRepository.findAll();
    }

    public List<AccountDtoView> getAccountDtos() {
        return this.accountRepository.getAccountDtos();
    }

    public Account findById(String email) {
        return this.accountRepository.findById(email)
                .orElseThrow(() -> new ObjectNotFoundException("account", email));
    }

    public boolean validateEmailAvailable(String email) {
        boolean isEmailAvailable = this.accountRepository.validateEmailAvailable(email);
        if (!isEmailAvailable)
            throw new EmailNotFreeException(email);
        return true;
    }

    public AccountDtoView getAccountDtoByEmail(String accountEmail) {
        return this.accountRepository.getAccountDtoByEmail(accountEmail)
                .orElseThrow(() -> new ObjectNotFoundException("account", accountEmail));
    }

    public Account save(Account account) {
        if (account == null)
            throw new NullPointerException("Account object cannot be null");

        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    // TODO: 10/3/2024 Check test methods, update if necessary
    public Account register(AccountDtoFormRegister accountDtoFormRegister) throws IOException, URISyntaxException {
        if (accountDtoFormRegister == null)
            throw new NullPointerException("Account object cannot be null");

        if (this.accountRepository.findAccountByEmail(accountDtoFormRegister.email()).isPresent()) {
            System.out.println("if condition false");
            throw new EmailAlreadyExistsException();
        }

        Account newAccount = new Account();
        newAccount.setEmail(accountDtoFormRegister.email());
        newAccount.setPassword(this.passwordEncoder.encode(accountDtoFormRegister.password()));
        newAccount.setRoles(accountDtoFormRegister.roles());

        Account savedAccount = this.accountRepository.save(newAccount);
        this.verificationService.requestVerificationEmail(savedAccount.getEmail());

        return savedAccount;
//        return this.accountRepository.save(newAccount);
    }

    public Account update(String accountEmail, AccountUpdateDtoForm update) {
        Account foundAccount = this.accountRepository.findAccountByEmail(accountEmail)
                .orElseThrow(() -> new ObjectNotFoundException("account", accountEmail));

        String rolesFixed = update.roles().replace("\"", "");
        foundAccount.setRoles(rolesFixed);
        return this.accountRepository.save(foundAccount);
    }

    public void delete(String email) {
        Account foundAccount = this.accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("account", email));
        this.accountRepository.delete(foundAccount);
    }

    /**
     * AccountDetails is fetched to Spring Security to check authentication.
     *
     * @param email Is used to find the Account object that tries to log in.
     * @return AccountDetails Here are the credentials that are being matched with
     * the provided username and password by the user when trying to log in.
     * @throws UsernameNotFoundException If the Account object doesn't exist, this
     *                                   exception is being thrown.
     */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.accountRepository.findAccountByEmail(email) // First, we need to find this user from database.
                .map(MyAccountPrincipal::new) // If found, wrap the returned user instance in a MyAccountPrincipal instance.
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " is not found"));
    }
}
