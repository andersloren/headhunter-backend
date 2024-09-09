package se.sprinta.headhunterbackend.ad;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
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

  @Query("SELECT new se.sprinta.headhunterbackend.ad.dto.AdDtoView(ad.id, ad.dateCreated, ad.htmlCode) FROM Ad ad")
  List<AdDtoView> getAdDtos();

  @Query("SELECT ad FROM Ad ad WHERE ad.job.id = :jobId")
  List<Ad> getAdsByJobId(Long jobId);

  @Query("SELECT new se.sprinta.headhunterbackend.ad.dto.AdDtoView(ad.id, ad.dateCreated, ad.htmlCode) FROM Ad ad WHERE ad.job.id = :jobId")
  List<AdDtoView> getAdDtosByJobId(long jobId);

  @Query("SELECT new se.sprinta.headhunterbackend.account.dto.AccountDtoView(ad.job.account.email, ad.job.account.roles, ad.job.account.number_of_jobs) FROM Ad ad where ad.id = :adId")
  AccountDtoView getAccountDtoByAdId(String adId);

  @Query("SELECT COUNT(ad) FROM Ad ad WHERE ad.job.id = :jobId")
  long getNumberOfAdsByJobId(long jobId);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM ad", nativeQuery = true)
  void deleteAdTable();
}
