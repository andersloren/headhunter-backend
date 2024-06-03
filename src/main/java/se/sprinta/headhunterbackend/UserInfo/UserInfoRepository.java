package se.sprinta.headhunterbackend.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.user.User;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    @Query("SELECT u FROM UserInfo u WHERE u.user.email = :email")
    Optional<UserInfo> getUserInfo(String email);
}
