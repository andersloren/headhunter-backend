package se.sprinta.headhunterbackend.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Account objects
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Returns a Account object.
     * Note that Email is the id of an Account object.
     */

    Optional<Account> findAccountByEmail(String email);

    @Query("SELECT COUNT(ac) = 0 FROM Account ac WHERE ac.email = :email")
    boolean isEmailAvailable(String email);

    @Query("SELECT new se.sprinta.headhunterbackend.account.dto.AccountDtoView(ac.email, ac.roles, ac.number_of_jobs) FROM Account ac where ac.email = :email")
    Optional<AccountDtoView> getAccountDtoByEmail(String email);

    @Query("SELECT new se.sprinta.headhunterbackend.account.dto.AccountDtoView(ac.email, ac.roles, ac.number_of_jobs) FROM Account ac")
    List<AccountDtoView> getAllAccountDtoViews();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Account", nativeQuery = true)
    void deleteAccountTable();
}
