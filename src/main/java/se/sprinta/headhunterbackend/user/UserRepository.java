package se.sprinta.headhunterbackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User objects
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Returns a User object.
     * Note that Email is the id of a User object.
     */

    Optional<User> findByEmail(String email);
}
