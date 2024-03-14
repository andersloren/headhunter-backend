package se.sprinta.headhunterbackend.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Job objects
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * Returns Job objects that are related a User object
     * Relationship: [Job] *...1 [User]
     * <p>
     * * Note that Email is the id of a User object.
     */

    List<Job> findAllByUser_Email(String email);
}
