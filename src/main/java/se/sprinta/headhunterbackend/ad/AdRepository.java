package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;

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

    @Query("SELECT new se.sprinta.headhunterbackend.ad.dto.AdDtoView(ad.id, ad.createdDateTime, ad.htmlCode) FROM Ad ad")
    List<AdDtoView> getAllAdDtos();

    @Query("SELECT ad FROM Ad ad WHERE ad.job.id = :jobId")
    List<Ad> getAdsByJobId(Long jobId);

    @Query("SELECT new se.sprinta.headhunterbackend.ad.dto.AdDtoView(ad.id, ad.createdDateTime, ad.htmlCode) FROM Ad ad WHERE ad.job.id = :jobId")
    List<AdDtoView> getAdDtosByJobId(long jobId);

    @Query("SELECT a.job.account FROM Ad a where a.id = :adId")
    Account getAccountByAdId(String adId);

    @Query("SELECT COUNT(ad) FROM Ad ad WHERE ad.job.id = :jobId")
    long getNumberOfAds(long jobId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Ad", nativeQuery = true)
    void deleteAdTable();
}
