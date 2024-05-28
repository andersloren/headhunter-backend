package se.sprinta.headhunterbackend.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Ad objects
 */

@Repository
public interface AdRepository extends JpaRepository<Ad, String> {

    /**
     * Returns Ad objects that are related a Job object
     * Relationship: [Ad] *...1 [Job]
     */

    List<Ad> findByJob_Id(Long jobId);

    @Query("SELECT COUNT(a) FROM Ad a WHERE a.job.id = :jobId")
    Long getNumberOfAds(Long jobId);
}
