package se.sprinta.headhunterbackend.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final JobService jobService;

    private final JdbcTemplate jdbcTemplate;

    public DBDataInitializer(UserService userService, JobService jobService, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jobService = jobService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        jdbcTemplate.execute("TRUNCATE TABLE jobs");

        /*
         * Users
         */

        User u1 = new User();
        u1.setEmail("m@e.se");
        u1.setUsername("Mikael");
        u1.setPassword("a");
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setEmail("a@l.se");
        u2.setUsername("Anders");
        u2.setPassword("a");
        u2.setRoles("user");

        this.userService.save(u1);
        this.userService.save(u2);

        /*
         * Jobs
         */

        JobDtoFormAdd job1 = new JobDtoFormAdd("m@e.se", "Testare till Tesla", "instruction");
        JobDtoFormAdd job2 = new JobDtoFormAdd("a@l.se", "Pilot till GPT", "instruction");
        JobDtoFormAdd job3 = new JobDtoFormAdd("m@e.se", "Programledare\\nIT Program-/Projektledare\\n\\n\\nVi söker en programledare för att leda och styra projektresurser vars focus är att bidra till den totala leveransen inom ett mer omfattande IT-program.\\n\\nSom programledare förväntas du hantera och facilitera externa intressenter som har beroenden till programmet.\\n\\n\\n\\nRollen kräver en kombination av teknisk expertis, dokumenterad projektledningsförmåga av större dignitet och ett tydligt affärsmannaskap.\\n\\nDu kommer övervaka planering, genomförande och leverans för att med intressenter, teammedlemmar och ledare säkerställa att programmet når sina mål.\\n\\n\\n\\n\\nProgrammet syftar till att ta in en ny kund till Soltak och att få alla delarna i den totala IT-leveransen på plats.\\n\\nFlertalet projekt är redan uppstartade och det finns även delar som ännu inte är uppstartade.\\n\\n\\n\\n\\nRollen behöver tillsättas omgående och initialt upptar programledarrollen 50% av en FTE.\\n\\nMöjlighet till utökande av projektledning av ett eller flera andra projekt.", "instruction");
        JobDtoFormAdd job4 = new JobDtoFormAdd("m@e.se", "Applikationsspecialist. SwCG söker en Applikationsspecialist till kund i Jönköping. Applikationsspecialist i förvaltningsgrupp eHälsa är en mångfacetterad roll med olika beröringspunkter inom systemförvaltning och verksamhetsutveckling. Du får möjlighet att förkovra dig inom sjukvårdens olika verksamhetsområden och dess systemstöd, leda uppdrag och stötta våra kunder med felsökning och problemlösning. Applikationsspecialisten stöttar även verksamheten kring systemförvaltning och är en länk mellan slutanvändare, systemförvaltare och oss på IT-drift. I större projekt kan du komma att delta och ha ett mer kravorienterat fokus och vara med kring anpassning av lösningen, samt bidra att projektet levererar nödvändiga leverabler som till exempel systemdokumentation till förvaltningsorganisationen. Vi jobbar agilt och målfokuserat tillsammans och du samarbetar tätt med teamets övriga kompetenser och andra förvaltningsgrupper inom IT-centrum, för att leverera väl fungerande tjänster och god service till våra kunder på sjukhusen och andra vårdgivare i länet. Uppdraget ska genomföras av en konsult och uppdragets omfattning är 60-100 % av en heltidstjänst. Offererad konsult ska uppfylla följande obligatoriska krav: Relevant IT-utbildning, Dokumenterad erfarenhet av att beskriva och dokumentera lösningar, Dokumenterad erfarenhet av test av applikationer, Dokumenterad erfarenhet av arbete med vårdrelaterade IT-system. Har arbetat och verkat hos en Region. Då uppdraget kommer att innebära mycket kontakt med medarbetare inom Region Jönköpings län kommer stor vikt att läggas vid personlig lämplighet. Offererad konsult ska uppfylla följande krav: Är drivande och har förmåga att hålla ihop mindre uppdrag, Är strukturerad med god analytisk- och problemlösningsförmåga, God kommunikativ förmåga, Social och har lätt för att arbeta i team, Förmåga att omsätta krav i realiserbart IT-stöd, God förståelse för hela systemutvecklingskedjan, Meriterande kompetens: Dokumenterad erfarenhet av arbete inom förvaltningsområdet eHälsa hos Region Jönköpings län och då några av följande system; Blå appen, Carelink, Checkware, Journalia, Medidoc, Obstetrix, Picsara.", "Skapa en professionell jobbannons i HTML med branchrelevant styling.");

        this.jobService.addJob(job1);
        this.jobService.addJob(job2);
        this.jobService.addJob(job3);
        this.jobService.addJob(job4);

    }
}
