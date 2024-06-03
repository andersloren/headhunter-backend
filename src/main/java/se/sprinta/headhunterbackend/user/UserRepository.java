package se.sprinta.headhunterbackend.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

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

    @Query("SELECT new se.sprinta.headhunterbackend.user.dto.UserDtoView(u.email, u.roles, u.number_of_jobs) FROM User u where u.email = :email")
    Optional<UserDtoView> getUserByEmail(String email);



}
