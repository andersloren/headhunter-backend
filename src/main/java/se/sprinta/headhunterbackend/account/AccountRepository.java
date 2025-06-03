package se.sprinta.headhunterbackend.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * Returns an Account object.
     * Note that Email is the id of an Account object.
     */

    Optional<Account> findAccountByEmail(@Param("email") String email);

    @Query("SELECT COUNT(ac) = 0 FROM Account ac WHERE ac.email = :email")
    boolean validateEmailAvailable(@Param("email") String email);

    @Query("SELECT new se.sprinta.headhunterbackend.account.dto.AccountDtoView(" +
            "ac.email AS email, " +
            "ac.roles AS roles, " +
            "ac.number_of_jobs AS number_of_jobs, " +
            "ac.isVerified AS isVerified)" +
            "FROM Account ac WHERE ac.email = :email")
    Optional<AccountDtoView> getAccountDtoByEmail(@Param("email") String email);

    @Query("SELECT new se.sprinta.headhunterbackend.account.dto.AccountDtoView(" +
            "ac.email AS email, " +
            "ac.roles AS roles, " +
            "ac.number_of_jobs AS number_of_jobs, " +
            "ac.isVerified AS isVerified) " +
            "FROM Account ac")
    List<AccountDtoView> getAccountDtos();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM account", nativeQuery = true)
    void deleteAccountTable();
}