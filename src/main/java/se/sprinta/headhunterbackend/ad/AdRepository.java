package se.sprinta.headhunterbackend.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, String> {

//    @Query("SELECT * FROM Ad WHERE ad.job.id = :id")
    List<Ad> findByJob_Id(Long jobId);
}
