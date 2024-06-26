package se.sprinta.headhunterbackend.accountInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, String> {

    @Query("SELECT ai FROM AccountInfo ai WHERE ai.account.email = :email")
    Optional<AccountInfo> getAccountInfo(String email);
}
