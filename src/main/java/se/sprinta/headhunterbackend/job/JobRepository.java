package se.sprinta.headhunterbackend.job;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT new se.sprinta.headhunterbackend.job.dto.JobDtoView(j.title, j.description, j.recruiterName, j.adCompany, j.adEmail, j.adPhone, j.applicationDeadline) FROM Job j WHERE j.account.email = :userEmail")
    List<JobDtoView> getJobDtosByUserEmail(String userEmail);

    @Query("SELECT new se.sprinta.headhunterbackend.job.dto.JobDtoView(j.title, j.description, j.recruiterName, j.adCompany, j.adEmail, j.adPhone, j.applicationDeadline) FROM Job j")
    List<JobDtoView> getJobDtos();

    @Query("SELECT new se.sprinta.headhunterbackend.job.dto.JobDtoView(j.title, j.description, j.recruiterName, j.adCompany, j.adEmail, j.adPhone, j.applicationDeadline) FROM Job j WHERE j.id = :jobId")
    Optional<JobDtoView> getFullJobDtoByJobId(Long jobId);

    @Query("SELECT new se.sprinta.headhunterbackend.job.dto.JobCardDtoView(j.id, j.title, j.applicationDeadline) FROM Job j WHERE j.account.email =:email")
    List<JobCardDtoView> getJobCardDtosByUserEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Job", nativeQuery = true)
    void deleteJobTable();
}
