package se.sprinta.headhunterbackend.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.user.User;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, String> {
    List<Ad> findByJob_Id(Long jobId);
}
