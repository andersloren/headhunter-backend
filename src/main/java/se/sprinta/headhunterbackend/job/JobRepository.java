package se.sprinta.headhunterbackend.job;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // Custom database queries can be added here if necessary
}
