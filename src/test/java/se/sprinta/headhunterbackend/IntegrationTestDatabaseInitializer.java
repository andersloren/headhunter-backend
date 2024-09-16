
package se.sprinta.headhunterbackend;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountService;
import se.sprinta.headhunterbackend.ad.AdService;
import se.sprinta.headhunterbackend.ad.dto.AdDtoForm;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;

/**
 * * Database entries (User objects, Job objects, and Ad objects) for testing.
 */

@Component
@Profile("integration-test")
@Transactional
@Disabled
public class IntegrationTestDatabaseInitializer implements CommandLineRunner {

    private final AccountService accountService;
    private final JobService jobService;
    private final AdService adService;

    public IntegrationTestDatabaseInitializer(AccountService accountService, JobService jobService, AdService adService) {
        this.accountService = accountService;
        this.jobService = jobService;
        this.adService = adService;
    }

    @Override
    public void run(String... args) throws Exception {

        /*
         * Accounts to be persisted
         */

        Account user1 = new Account();
        user1.setEmail("user1-integrationTest@hh.se");
        user1.setPassword("a");
        user1.setRoles("user");

        Account user2 = new Account();
        user2.setEmail("user2-integrationTest@hh.se");
        user2.setPassword("a");
        user2.setRoles("user");

        Account user3 = new Account();
        user3.setEmail("user3-integrationTest@hh.se");
        user3.setPassword("a");
        user3.setRoles("user");

        Account admin = new Account();
        admin.setEmail("admin-integrationTest@hh.se");
        admin.setPassword("a");
        admin.setRoles("admin");

        this.accountService.save(admin);
        this.accountService.save(user1);
        this.accountService.save(user2);
        this.accountService.save(user3);

        /*
         * Jobs to be persisted
         */

        Job job1 = new Job(
                "job1 Title 1",
                "job1 Description 1",
                "job1 Instruction 1");
        Job job2 = new Job(
                "job2 Title 2",
                "job2 Description 2",
                "job2 Instruction 2");
        Job job3 = new Job(
                "job3 Title 3",
                "job3 Description 3",
                "job3 Instruction 3");
        Job job4 = new Job(
                "Fullstack Utvecklare",
                "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.",
                "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.");

        this.jobService.addJob("user1-integrationTest@hh.se", job1);
        this.jobService.addJob("user1-integrationTest@hh.se", job2);
        this.jobService.addJob("user2-integrationTest@hh.se", job3);
        this.jobService.addJob("user3-integrationTest@hh.se", job4);

        /*
         * Ads to be persisted
         */

        AdDtoForm ad1 = new AdDtoForm("htmlCode 1");
        AdDtoForm ad2 = new AdDtoForm("htmlCode 2");
        AdDtoForm ad3 = new AdDtoForm("htmlCode 3");

        this.adService.addAd(1L, ad1);
        this.adService.addAd(1L, ad2);
        this.adService.addAd(2L, ad3);
    }
}
