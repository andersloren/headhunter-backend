package se.sprinta.headhunterbackend.system;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
@Transactional
@Profile("job-test")
public class DBJobInitializerForTest implements CommandLineRunner {

    private final UserService userService;
    private final JobService jobService;

    public DBJobInitializerForTest(UserService userService, JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    @Override
    public void run(String... args) {
        User user1 = new User();
        user1.setEmail("m@e.se");
        user1.setUsername("Mikael");
        user1.setPassword("a");
        user1.setRoles("admin user");

        User user2 = new User();
        user2.setEmail("a@l.se");
        user2.setUsername("Anders");
        user2.setPassword("a");
        user2.setRoles("user");

        String instruction = "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.";

        Job job1 = this.jobService.addJob(new JobDtoFormAdd(
                "m@e.se",
                "Testare till Tesla",
                "En massa nya funktioner behöver testas. Den anställde behöver vara uthållig och driven.",
                instruction));

        Job job2 = this.jobService.addJob(new JobDtoFormAdd(
                "m@e.se",
                "AI-utvecklare till GPTPilot."
                , "Hög analytisk förmåga. Doktorand i matematisk statistik. Mångårig erfarenhet av AI-system.",
                instruction));

        Job job3 = this.jobService.addJob(new JobDtoFormAdd(
                "m@e.se",
                "Programledare IT Program-/Projektledare",
                " Vi söker en programledare för att leda och styra projektresurser vars focus är att bidra till den totala leveransen inom ett mer omfattande IT-program.\\Som programledare förväntas du hantera och facilitera externa intressenter som har beroenden till programmet.Rollen kräver en kombination av teknisk expertis, dokumenterad projektledningsförmåga av större dignitet och ett tydligt affärsmannaskap.Du kommer övervaka planering, genomförande och leverans för att med intressenter, teammedlemmar och ledare säkerställa att programmet når sina mål.Programmet syftar till att ta in en ny kund till Soltak och att få alla delarna i den totala IT-leveransen på plats.\\Flertalet projekt är redan uppstartade och det finns även delar som ännu inte är uppstartade.Rollen behöver tillsättas omgående och initialt upptar programledarrollen 50% av en FTE.Möjlighet till utökande av projektledning av ett eller flera andra projekt.",
                instruction));

        Job job4 = this.jobService.addJob(new JobDtoFormAdd(
                "a@l.se", "Applikationsspecialist",
                "SwCG söker en Applikationsspecialist till kund i Jönköping. Applikationsspecialist i förvaltningsgrupp eHälsa är en mångfacetterad roll med olika beröringspunkter inom systemförvaltning och verksamhetsutveckling. Du får möjlighet att förkovra dig inom sjukvårdens olika verksamhetsområden och dess systemstöd, leda uppdrag och stötta våra kunder med felsökning och problemlösning. Applikationsspecialisten stöttar även verksamheten kring systemförvaltning och är en länk mellan slutanvändare, systemförvaltare och oss på IT-drift. I större projekt kan du komma att delta och ha ett mer kravorienterat fokus och vara med kring anpassning av lösningen, samt bidra att projektet levererar nödvändiga leverabler som till exempel systemdokumentation till förvaltningsorganisationen. Vi jobbar agilt och målfokuserat tillsammans och du samarbetar tätt med teamets övriga kompetenser och andra förvaltningsgrupper inom IT-centrum, för att leverera väl fungerande tjänster och god service till våra kunder på sjukhusen och andra vårdgivare i länet. Uppdraget ska genomföras av en konsult och uppdragets omfattning är 60-100 % av en heltidstjänst. Offererad konsult ska uppfylla följande obligatoriska krav: Relevant IT-utbildning, Dokumenterad erfarenhet av att beskriva och dokumentera lösningar, Dokumenterad erfarenhet av test av applikationer, Dokumenterad erfarenhet av arbete med vårdrelaterade IT-system. Har arbetat och verkat hos en Region. Då uppdraget kommer att innebära mycket kontakt med medarbetare inom Region Jönköpings län kommer stor vikt att läggas vid personlig lämplighet. Offererad konsult ska uppfylla följande krav: Är drivande och har förmåga att hålla ihop mindre uppdrag, Är strukturerad med god analytisk- och problemlösningsförmåga, God kommunikativ förmåga, Social och har lätt för att arbeta i team, Förmåga att omsätta krav i realiserbart IT-stöd, God förståelse för hela systemutvecklingskedjan, Meriterande kompetens: Dokumenterad erfarenhet av arbete inom förvaltningsområdet eHälsa hos Region Jönköpings län och då några av följande system; Blå appen, Carelink, Checkware, Journalia, Medidoc, Obstetrix, Picsara.",
                instruction));

        Job job5 = this.jobService.addJob(new JobDtoFormAdd("a@l.se",
                "Utvecklare AI-app",
                "En applikation för AI-genererade jobbannonser. Frontend är skriven i React, och tillhörande libraries är React Router, DOMPurify, CSS och Styled Components. Backend är skriven i Java och Spring Boot, Spring Security samt kommunikation med ett AI API används. Databasen sköts av MySQL. Applikationen styrs genom molntjänsten Azure. GIT och GitHub används som versionshanterare. Alla inblandade behöver kontinuerligt vara beredda på att sätta sig in i nya libraries och frameworks. Målet är att applikationen ska sjösättas inom 1 år. Applikationen växer snabbt och de nuvarande två utvecklarna får allt svårare att hinna med allt som behöver göras. Ytterligare en utvecklare behövs nu.",
                instruction));

        user1.addJob(job1);
        user1.addJob(job2);
        user1.addJob(job3);
        user2.addJob(job4);
        user2.addJob(job5);

        this.userService.save(user1);
        this.userService.save(user2);
    }
}