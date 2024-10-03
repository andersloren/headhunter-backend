package se.sprinta.headhunterbackend.accountVerification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    @Query("SELECT v FROM Verification v WHERE v.account.email = :accountEmail")
    String findByEmail(String accountEmail);
}