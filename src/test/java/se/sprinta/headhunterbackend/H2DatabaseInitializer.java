package se.sprinta.headhunterbackend;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.account.converter.AccountToAccountDtoViewConverter;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.AdRepository;
import se.sprinta.headhunterbackend.ad.converter.AdToAdDtoView;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.converter.JobToJobCardDtoViewConverter;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("test-h2")
public class H2DatabaseInitializer {

  @Autowired
  private AccountToAccountDtoViewConverter accountToAccountDtoViewConverter;

  @Autowired
  private JobToJobDtoViewConverter jobToJobDtoViewConverter;

  @Autowired
  private JobToJobCardDtoViewConverter jobToJobCardDtoViewConverter;

  @Autowired
  private AdToAdDtoView adToAdDtoView;

  @Autowired
  private final AccountRepository accountRepository;

  @Autowired
  private final JobRepository jobRepository;

  @Autowired
  private final AdRepository adRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Getter
  private static List<Account> accounts = new ArrayList<>();
  @Getter
  private static List<Job> jobs = new ArrayList<>();
  @Getter
  private static List<JobDtoView> jobDtos = new ArrayList<>();
  @Getter
  private static List<JobCardDtoView> jobCardDtoViews = new ArrayList<>();
  @Getter
  private static List<AccountDtoView> accountDtos = new ArrayList<>();
  @Getter
  private static List<Ad> ads = new ArrayList<>();

  public H2DatabaseInitializer(AccountRepository accountRepository, JobRepository jobRepository,
      AdRepository adRepository) {
    this.accountRepository = accountRepository;
    this.jobRepository = jobRepository;
    this.adRepository = adRepository;
  }

  public void initializeH2Database() {

    // jdbcTemplate.execute("TRUNCATE TABLE ad");
    // jdbcTemplate.execute("TRUNCATE TABLE job");
    // jdbcTemplate.execute("TRUNCATE TABLE account");
    // jdbcTemplate.execute("ALTER TABLE job ALTER COLUMN id RESTART WITH 1");

    Account admin = new Account();
    admin.setEmail("admin-h2@hh.se");
    admin.setPassword("a");
    admin.setRoles("admin");

    Account user1 = new Account();
    user1.setEmail("user1-h2@hh.se");
    user1.setPassword("a");
    user1.setRoles("user");

    Account user2 = new Account();
    user2.setEmail("user2-h2@hh.se");
    user2.setPassword("a");
    user2.setRoles("user");

    Account user3 = new Account();
    user3.setEmail("user3-h2@hh.se");
    user3.setPassword("a");
    user3.setRoles("user");

    /**
     * Jobs to be persisted
     */

    Job job1 = new Job();
    job1.setTitle("job1 Title 1");
    job1.setDescription("job1 Description 1");
    job1.setApplicationDeadline("job1 applicationDeadline 1");
    job1.setInstruction("job1 Instruction 1");

    Job job2 = new Job();
    job2.setTitle("job2 Title 2");
    job2.setDescription("job2 Description 2");
    job2.setApplicationDeadline("job2 applicationDeadline 2");
    job2.setInstruction("job2 Instruction 2");

    Job job3 = new Job();
    job3.setTitle("job3 Title 3");
    job3.setDescription("job3 Description 3");
    job3.setApplicationDeadline("job3 applicationDeadline 3");
    job3.setInstruction("job3 Instruction 3");

    Job job4 = new Job();
    job4.setTitle("Fullstack Utvecklare");
    job4.setDescription(
        "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.");
    job4.setApplicationDeadline("job4 applicationDeadline 4");
    job4.setInstruction(
        "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.");

    /**
     * Ads to be persisted
     */

    Ad ad1 = new Ad("htmlCode 1");
    Ad ad2 = new Ad("htmlCode 2");
    Ad ad3 = new Ad("htmlCode 3");

    user1.addJob(job1);
    user1.addJob(job2);
    user2.addJob(job3);
    user3.addJob(job4);

    job1.setAccount(user1);
    job2.setAccount(user1);
    job3.setAccount(user2);
    job4.setAccount(user3);

    job1.addAd(ad1);
    job1.addAd(ad2);
    job2.addAd(ad3);

    ad1.setJob(job1);
    ad2.setJob(job1);
    ad3.setJob(job2);

    this.accountRepository.save(admin);
    this.accountRepository.save(user1);
    this.accountRepository.save(user2);
    this.accountRepository.save(user3);

    this.jobRepository.save(job1);
    this.jobRepository.save(job2);
    this.jobRepository.save(job3);
    this.jobRepository.save(job4);

    this.adRepository.save(ad1);
    this.adRepository.save(ad2);
    this.adRepository.save(ad3);

    accounts.add(admin);
    accounts.add(user1);
    accounts.add(user2);
    accounts.add(user3);

    jobs.add(job1);
    jobs.add(job2);
    jobs.add(job3);
    jobs.add(job4);

    ads.add(ad1);
    ads.add(ad2);
    ads.add(ad3);

  }

  public List<AccountDtoView> initializeH2AccountDtos() {
    return getAccounts().stream()
        .map(account -> this.accountToAccountDtoViewConverter.convert(account)).toList();
  }

  public List<JobDtoView> initializeH2JobDtos() {
    return getJobs().stream()
        .map(job -> this.jobToJobDtoViewConverter.convert(job)).toList();
  }

  public List<JobCardDtoView> initializeH2JobCardDtos() {
    return getJobs().stream()
        .map(job -> this.jobToJobCardDtoViewConverter.convert(job)).toList();
  }

  public List<AdDtoView> initializeH2AdDtos() {
    return getAds().stream()
        .map(ad -> this.adToAdDtoView.convert(ad)).toList();
  }

  @Transactional
  public void clearH2Database() {
    this.adRepository.deleteAdTable();
    this.jobRepository.deleteJobTable();
    this.accountRepository.deleteAccountTable();
  }
}
