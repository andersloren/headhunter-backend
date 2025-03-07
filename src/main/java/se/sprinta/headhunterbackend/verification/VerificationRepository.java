package se.sprinta.headhunterbackend.verification;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    @Query("SELECT v.verificationCode FROM Verification v WHERE v.account.email = :accountEmail")
    Optional<String> findByEmail(String accountEmail);

    @Query("SELECT v FROM Verification v WHERE v.account.email = :accountEmail")
    Optional<Verification> findVerificationByEmail(String accountEmail);

    @Modifying
    @Query("DELETE FROM Verification v WHERE v.account.email = :accountEmail")
    void deleteByEmail(String accountEmail);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM verification", nativeQuery = true)
    void deleteVerificationTable();
}