package se.sprinta.headhunterbackend;

import lombok.Getter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.accountVerification.Verification;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.dto.AdDtoView;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class MockDatabaseInitializer {

    private static List<Account> accounts = new ArrayList<>();
    private static final List<AccountDtoView> accountDtos = new ArrayList<>();

    private static List<Verification> verifications = new ArrayList<>();

    private static List<Job> jobs = new ArrayList<>();
    private static final List<JobDtoView> jobDtos = new ArrayList<>();
    private static final List<JobCardDtoView> jobCardDtos = new ArrayList<>();

    private static List<Ad> ads = new ArrayList<>();
    private static final List<AdDtoView> adDtos = new ArrayList<>();

    public static List<Account> initializeMockAccounts() {

        accounts.clear();

        Account user1 = new Account();
        user1.setEmail("user1-mock@hh.se");
        user1.setPassword("a");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2-mock@hh.se");
        user2.setPassword("a");
        user2.setRoles("user");

        Account user3 = new Account();
        user3.setEmail("user3-mock@hh.se");
        user3.setPassword("a");
        user3.setRoles("user");

        Account user4 = new Account();
        user4.setEmail("user4-mock@hh.se");
        user4.setPassword("a");
        user4.setRoles("user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("job1 Title 1");
        job1.setDescription("job1 Description 1");
        job1.setApplicationDeadline("job1 applicationDeadline 1");
        job1.setInstruction("job1 Instruction 1");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("job2 Title 2");
        job2.setDescription("job2 Description 2");
        job2.setApplicationDeadline("job2 applicationDeadline 2");
        job2.setInstruction("job2 Instruction 2");

        Job job3 = new Job();
        job3.setId(3L);
        job3.setTitle("job3 Title 3");
        job3.setDescription("job3 Description 3");
        job3.setApplicationDeadline("job3 applicationDeadline 3");
        job3.setInstruction("job3 Instruction 3");

        Job job4 = new Job();
        job4.setId(4L);
        job4.setTitle("Fullstack Utvecklare");
        job4.setDescription(
                "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.");
        job4.setApplicationDeadline("job4 applicationDeadline 4");
        job4.setInstruction(
                "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.");

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

        accounts.clear();
        accounts.add(user1);
        accounts.add(user2);
        accounts.add(user3);
        accounts.add(user4);

        jobs.clear();
        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);

        ads.clear();
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);

        return accounts;
    }

    public static List<Verification> initializeMockVerifications() {

        verifications.clear();

        Account user1 = new Account();
        user1.setEmail("user1-mock@hh.se");
        user1.setPassword("a");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2-mock@hh.se");
        user2.setPassword("a");
        user2.setRoles("user");

        Account user3 = new Account();
        user3.setEmail("user3-mock@hh.se");
        user3.setPassword("a");
        user3.setRoles("user");

        Account user4 = new Account();
        user4.setEmail("user4-mock@hh.se");
        user4.setPassword("a");
        user4.setRoles("user");

        Verification verification1 = new Verification();
        verification1.setId(1L);
        verification1.setAccount(user1);
        verification1.setVerificationCode("12345");

        Verification verification2 = new Verification();
        verification2.setId(2L);
        verification2.setAccount(user2);
        verification2.setVerificationCode("23456");

        Verification verification3 = new Verification();
        verification3.setId(3L);
        verification3.setAccount(user3);
        verification3.setVerificationCode("34567");

        Verification verification4 = new Verification();
        verification4.setId(4L);
        verification4.setAccount(user4);
        verification4.setVerificationCode("45678");

        verifications.add(verification1);
        verifications.add(verification2);
        verifications.add(verification3);
        verifications.add(verification4);

        return verifications;
    }

    public static List<Job> initializeMockJobs() {

        jobs.clear();

        Account user1 = new Account();
        user1.setEmail("user1-mock@hh.se");
        user1.setPassword("a");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2-mock@hh.se");
        user2.setPassword("a");
        user2.setRoles("user");

        Account user3 = new Account();
        user3.setEmail("user3-mock@hh.se");
        user3.setPassword("a");
        user3.setRoles("user");

        Account user4 = new Account();
        user4.setEmail("user4-mock@hh.se");
        user4.setPassword("a");
        user4.setRoles("user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("job1 Title 1");
        job1.setDescription("job1 Description 1");
        job1.setApplicationDeadline("job1 applicationDeadline 1");
        job1.setInstruction("job1 Instruction 1");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("job2 Title 2");
        job2.setDescription("job2 Description 2");
        job2.setApplicationDeadline("job2 applicationDeadline 2");
        job2.setInstruction("job2 Instruction 2");

        Job job3 = new Job();
        job3.setId(3L);
        job3.setTitle("job3 Title 3");
        job3.setDescription("job3 Description 3");
        job3.setApplicationDeadline("job3 applicationDeadline 3");
        job3.setInstruction("job3 Instruction 3");

        Job job4 = new Job();
        job4.setId(4L);
        job4.setTitle("Fullstack Utvecklare");
        job4.setDescription(
                "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.");
        job4.setApplicationDeadline("job4 applicationDeadline 4");
        job4.setInstruction(
                "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.");

        Ad ad1 = new Ad("id 1", "htmlCode 1");
        Ad ad2 = new Ad("id 2", "htmlCode 2");
        Ad ad3 = new Ad("id 3", "htmlCode 3");

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

        accounts.add(user1);
        accounts.add(user2);
        accounts.add(user3);
        accounts.add(user4);

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);

        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);

        return jobs;
    }

    public static List<Ad> initializeMockAds() {

        ads.clear();

        Account user1 = new Account();
        user1.setEmail("user1-mock@hh.se");
        user1.setPassword("a");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2-mock@hh.se");
        user2.setPassword("a");
        user2.setRoles("user");

        Account user3 = new Account();
        user3.setEmail("user3-mock@hh.se");
        user3.setPassword("a");
        user3.setRoles("user");

        Account user4 = new Account();
        user4.setEmail("user4-mock@hh.se");
        user4.setPassword("a");
        user4.setRoles("user");

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("job1 Title 1");
        job1.setDescription("job1 Description 1");
        job1.setApplicationDeadline("job1 applicationDeadline 1");
        job1.setInstruction("job1 Instruction 1");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("job2 Title 2");
        job2.setDescription("job2 Description 2");
        job2.setApplicationDeadline("job2 applicationDeadline 2");
        job2.setInstruction("job2 Instruction 2");

        Job job3 = new Job();
        job3.setId(3L);
        job3.setTitle("job3 Title 3");
        job3.setDescription("job3 Description 3");
        job3.setApplicationDeadline("job3 applicationDeadline 3");
        job3.setInstruction("job3 Instruction 3");

        Job job4 = new Job();
        job4.setId(4L);
        job4.setTitle("Fullstack Utvecklare");
        job4.setDescription(
                "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.");
        job4.setApplicationDeadline("job4 applicationDeadline 4");
        job4.setInstruction(
                "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.");

        Ad ad1 = new Ad("id 1", "htmlCode 1");
        Ad ad2 = new Ad("id 2", "htmlCode 2");
        Ad ad3 = new Ad("id 3", "htmlCode 3");

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

        accounts.add(user1);
        accounts.add(user2);
        accounts.add(user3);
        accounts.add(user4);

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);

        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);

        return ads;
    }

    public static List<AccountDtoView> initializeMockAccountDtos() {
        accountDtos.clear();
        accounts.clear();
        accounts = initializeMockAccounts();

        AccountDtoView accountDto1 = new AccountDtoView(
                accounts.get(0).getEmail(),
                accounts.get(0).getRoles(),
                accounts.get(0).getNumber_of_jobs());

        AccountDtoView accountDto2 = new AccountDtoView(
                accounts.get(1).getEmail(),
                accounts.get(1).getRoles(),
                accounts.get(1).getNumber_of_jobs());

        AccountDtoView accountDto3 = new AccountDtoView(
                accounts.get(2).getEmail(),
                accounts.get(2).getRoles(),
                accounts.get(2).getNumber_of_jobs());

        AccountDtoView accountDto4 = new AccountDtoView(
                accounts.get(3).getEmail(),
                accounts.get(3).getRoles(),
                accounts.get(3).getNumber_of_jobs());

        accountDtos.add(accountDto1);
        accountDtos.add(accountDto2);
        accountDtos.add(accountDto3);
        accountDtos.add(accountDto4);

        return accountDtos;
    }

    public static List<JobDtoView> initializeMockJobDtos() {
        jobDtos.clear();
        jobs.clear();
        jobs = initializeMockJobs();

        JobDtoView jobDto1 = new JobDtoView(
                jobs.get(0).getTitle(),
                jobs.get(0).getDescription(),
                jobs.get(0).getRecruiterName(),
                jobs.get(0).getAdCompany(),
                jobs.get(0).getAdEmail(),
                jobs.get(0).getAdPhone(),
                jobs.get(0).getApplicationDeadline());

        JobDtoView jobDto2 = new JobDtoView(
                jobs.get(1).getTitle(),
                jobs.get(1).getDescription(),
                jobs.get(1).getRecruiterName(),
                jobs.get(1).getAdCompany(),
                jobs.get(1).getAdEmail(),
                jobs.get(1).getAdPhone(),
                jobs.get(1).getApplicationDeadline());

        JobDtoView jobDto3 = new JobDtoView(
                jobs.get(2).getTitle(),
                jobs.get(2).getDescription(),
                jobs.get(2).getRecruiterName(),
                jobs.get(2).getAdCompany(),
                jobs.get(2).getAdEmail(),
                jobs.get(2).getAdPhone(),
                jobs.get(2).getApplicationDeadline());

        JobDtoView jobDto4 = new JobDtoView(
                jobs.get(3).getTitle(),
                jobs.get(3).getDescription(),
                jobs.get(3).getRecruiterName(),
                jobs.get(3).getAdCompany(),
                jobs.get(3).getAdEmail(),
                jobs.get(3).getAdPhone(),
                jobs.get(3).getApplicationDeadline());

        jobDtos.add(jobDto1);
        jobDtos.add(jobDto2);
        jobDtos.add(jobDto3);
        jobDtos.add(jobDto4);

        return jobDtos;
    }

    public static List<JobCardDtoView> initializeMockJobCardDtos() {
        jobCardDtos.clear();
        jobs.clear();
        jobs = initializeMockJobs();

        JobCardDtoView jobDCardDto1 = new JobCardDtoView(
                1L,
                jobs.get(0).getTitle(),
                jobs.get(0).getApplicationDeadline());

        JobCardDtoView jobDCardDto2 = new JobCardDtoView(
                2L,
                jobs.get(1).getTitle(),
                jobs.get(1).getApplicationDeadline());

        JobCardDtoView jobDCardDto3 = new JobCardDtoView(
                3L,
                jobs.get(2).getTitle(),
                jobs.get(2).getApplicationDeadline());

        JobCardDtoView jobDCardDto4 = new JobCardDtoView(
                4L,
                jobs.get(3).getTitle(),
                jobs.get(3).getApplicationDeadline());

        jobCardDtos.add(jobDCardDto1);
        jobCardDtos.add(jobDCardDto2);
        jobCardDtos.add(jobDCardDto3);
        jobCardDtos.add(jobDCardDto4);

        return jobCardDtos;
    }

    public static JobDtoFormAdd initializeMockJobDtoFormAdd() {

        return new JobDtoFormAdd(
                "Title Mock",
                "Description Mock",
                "Instruction Mock");
    }

    public static JobDtoFormUpdate initializeMockJobDtoFormUpdate() {

        return new JobDtoFormUpdate(
                "Updated Title Mock",
                "Updated Description Mock",
                "Updated Instruction Mock",
                "Updated RecruiterName Mock",
                "Updated adCompany Mock",
                "Updated adEmail Mock",
                "Updated adPhone Mock",
                "Updated adApplicationDeadline Mock");
    }

    public static List<AdDtoView> initializeMockAdDtos() {
        adDtos.clear();
        ads.clear();
        ads = initializeMockAds();

        AdDtoView adDto1 = new AdDtoView(
                ads.get(0).getId(),
                ads.get(0).getDateCreated(),
                ads.get(0).getHtmlCode());

        AdDtoView adDto2 = new AdDtoView(
                ads.get(1).getId(),
                ads.get(1).getDateCreated(),
                ads.get(1).getHtmlCode());

        AdDtoView adDto3 = new AdDtoView(
                ads.get(2).getId(),
                ads.get(2).getDateCreated(),
                ads.get(2).getHtmlCode());

        adDtos.add(adDto1);
        adDtos.add(adDto2);
        adDtos.add(adDto3);

        return adDtos;
    }
}
