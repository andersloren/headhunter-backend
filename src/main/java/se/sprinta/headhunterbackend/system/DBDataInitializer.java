package se.sprinta.headhunterbackend.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobController;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.job.JobService;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final JobService jobService;

    private final JdbcTemplate jdbcTemplate;
    private final JobRepository jobRepository;

    public DBDataInitializer(UserService userService, JobService jobService, JdbcTemplate jdbcTemplate, JobRepository jobRepository) {
        this.userService = userService;
        this.jobService = jobService;
        this.jdbcTemplate = jdbcTemplate;
        this.jobRepository = jobRepository;
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

        Job job1 = new Job(
                null,
                "Testare till Tesla. En massa nya funktioner behöver testas. Den anställde behöver vara uthållig och driven. ",
                u1, "Skapa en professionell jobbannons i HTML med branchrelevant styling.",
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "body {" +
                        "font-family: Arial, sans-serif;" +
                        "color: #333;" +
                        "background-color: #f7f7f7;" +
                        "padding: 20px;" +
                        "}" +
                        ".header {" +
                        "color: #ff6700;" +
                        "}" +
                        ".job-content {" +
                        "background-color: white;" +
                        "padding: 30px;" +
                        "border-radius: 10px;" +
                        "}" +
                        ".job-description {" +
                        "margin-top: 20px;" +
                        "}" +
                        ".requirements {" +
                        "margin-top: 20px;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1 class=\"header\">Jobbannons: Testare till Tesla</h1>" +
                        "<div class=\"job-content\">" +
                        "<h2>Om Jobbet:</h2>" +
                        "<p class=\"job-description\">Tesla är i konstant utveckling och en mängd nya funktioner behöver ständigt testas. Vi söker en uthållig och driven Testare som kan hjälpa oss med detta arbete och säkerställa att vi kan fortsätta att leverera produkter av hög kvalitet till våra kunder.</p>" +
                        "" +
                        "<h2>Krav:</h2>" +
                        "<ul class=\"requirements\">" +
                        "<li>Minst 2 års erfarenhet av testning och kvalitetssäkring.</li>" +
                        "<li>God förmåga att identifiera och lösa problem.</li>" +
                        "<li>Förmåga att jobba självständigt, men också i team.</li>" +
                        "</ul>" +
                        "" +
                        "<h2>Att ansöka:</h2>" +
                        "<p>För att ansöka till denna position, vänligen skicka ditt CV och personligt brev till jobb@tesla.com med ämnet \"Ansökan till Testare\".</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>"
        );
        Job job2 = new Job(
                null,
                "Pilot till GPT",
                u1,
                "Skapa en professionell jobbannons i HTML med branchrelevant styling.",
                "<html>" +
                        "<head>" +
                        "<title>Jobbannons för Pilot till GPT</title>" +
                        "<style>" +
                        "body {" +
                        "background-color: #f0f0f0;" +
                        "font-family: Arial, sans-serif;" +
                        "}" +
                        "" +
                        ".container {" +
                        "border: 1px solid gray;" +
                        "width:60%;" +
                        "margin: auto;" +
                        "padding: 20px;" +
                        "background-color: white;" +
                        "}" +
                        "" +
                        ".header {" +
                        "text-align: center;" +
                        "color: #AA3A3C;" +
                        "text-transform: uppercase;" +
                        "margin-bottom: 20px;" +
                        "}" +
                        "" +
                        ".content {" +
                        "color: black;" +
                        "text-align: justify;" +
                        "}" +
                        "" +
                        ".footer {" +
                        "border-top: 1px solid gray;" +
                        "margin-top: 20px;" +
                        "padding-top: 10px;" +
                        "text-align: right;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<div class=\"header\">" +
                        "<h1>Pilot till GPT</h1>" +
                        "</div>" +
                        "<div class=\"content\">" +
                        "<p>" +
                        "GPT söker entusiastiska och kvalificerade piloter att ansluta oss på våra globala resor. Den ideala kandidaten har en passion för luftfart," +
                        "en makalös strävan efter perfektion och en förmåga att arbeta effektivt som en del av ett team." +
                        "</p>" +
                        "" +
                        "<h2>Competenser:</h2>" +
                        "<ul>" +
                        "<li>Relevant Certifiering. </li>" +
                        "<li>Minst 3 års erfarenhet av kommersiell flygning. </li>" +
                        "<li>Utmärkta kommunikationsfärdigheter. </li>" +
                        "</ul>" +
                        "" +
                        "<h2>Ansvarsområden:</h2>" +
                        "<ul>" +
                        "<li>Utföra alla flyg- och markrelaterade uppgifter för att säkerställa en säker och effektiv drift. </li>" +
                        "<li>Utveckla och upprätthålla professionella relationer med arbetskamrater. </li>" +
                        "</ul>" +
                        " " +
                        "<h2>Vi erbjuder:</h2>" +
                        "<ul>" +
                        "<li>Marknadsmässig lön och förmåner. </li>" +
                        "<li>Möjlighet till personlig och professionell utveckling.</li>" +
                        "</ul>" +
                        "</div>" +
                        "<div class=\"footer\">" +
                        "<p>Ansök snarast! Notera att urvalet sker löpande.</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>"
        );
        Job job3 = new Job(
                null,
                "Programledare IT Program-/Projektledare nVi söker en programledare för att leda och styra projektresurser vars focus är att bidra till den totala leveransen inom ett mer omfattande IT-program.\\Som programledare förväntas du hantera och facilitera externa intressenter som har beroenden till programmet.Rollen kräver en kombination av teknisk expertis, dokumenterad projektledningsförmåga av större dignitet och ett tydligt affärsmannaskap.Du kommer övervaka planering, genomförande och leverans för att med intressenter, teammedlemmar och ledare säkerställa att programmet når sina mål.Programmet syftar till att ta in en ny kund till Soltak och att få alla delarna i den totala IT-leveransen på plats.\\Flertalet projekt är redan uppstartade och det finns även delar som ännu inte är uppstartade.Rollen behöver tillsättas omgående och initialt upptar programledarrollen 50% av en FTE.Möjlighet till utökande av projektledning av ett eller flera andra projekt.",
                u1,
                "Skapa en professionell jobbannons i HTML med branchrelevant styling.",
                "<!DOCTYPE html><html><head><title>Job Rent | Programledare</title><style type=\"text/css\">body{font-family: Arial, sans-serif;}h1,h2,h3{color: #333;margin-bottom: 20px;}p{margin-bottom: 10px;}.header{background-color: #f8f9fa;padding: 20px;text-align: center;}.content{padding: 0 20px;}.footer{background-color: #f8f9fa;padding: 20px;text-align: center;font-size: 12px;color: #7a7a7a;}</style></head><body><div class=\"header\"><h1>Programledare</h1><h2>IT Program-/Projektledare</h2></div><div class=\"content\"><p>Vi söker en programledare för att leda och styra projektresurser vars focus är att bidra till den totala leveransen inom ett mer omfattande IT-program.</p><p>Som programledare förväntas du hantera och facilitera externa intressenter som har beroenden till programmet.</p><h3>Kunskapskrav:</h3><p>Rollen kräver en kombination av teknisk expertis, dokumenterad projektledningsförmåga av större dignitet och ett tydligt affärsmannaskap.</p><p>Du kommer övervaka planering, genomförande och leverans för att med intressenter, teammedlemmar och ledare säkerställa att programmet når sina mål.</p><h3>Programmets Syfte:</h3><p>Programmet syftar till att ta in en ny kund till Soltak och att få alla delarna i den totala IT-leveransen på plats.</p><p>Flertalet projekt är redan uppstartade och det finns även delar som ännu inte är uppstartade.</p><h3>Startdatum & Avtalstyp:</h3><p>Rollen behöver tillsättas omgående och initialt upptar programledarrollen 50% av en FTE.</p><p>Möjlighet till utökande av projektledning av ett eller flera andra projekt.</p></div> <div class=\"footer\"><p>Soltak © 2021 | All rights reserved</p></div> </body></html>"
        );
        Job job4 = new Job(
                null,
                "Applikationsspecialist. SwCG söker en Applikationsspecialist till kund i Jönköping. Applikationsspecialist i förvaltningsgrupp eHälsa är en mångfacetterad roll med olika beröringspunkter inom systemförvaltning och verksamhetsutveckling. Du får möjlighet att förkovra dig inom sjukvårdens olika verksamhetsområden och dess systemstöd, leda uppdrag och stötta våra kunder med felsökning och problemlösning. Applikationsspecialisten stöttar även verksamheten kring systemförvaltning och är en länk mellan slutanvändare, systemförvaltare och oss på IT-drift. I större projekt kan du komma att delta och ha ett mer kravorienterat fokus och vara med kring anpassning av lösningen, samt bidra att projektet levererar nödvändiga leverabler som till exempel systemdokumentation till förvaltningsorganisationen. Vi jobbar agilt och målfokuserat tillsammans och du samarbetar tätt med teamets övriga kompetenser och andra förvaltningsgrupper inom IT-centrum, för att leverera väl fungerande tjänster och god service till våra kunder på sjukhusen och andra vårdgivare i länet. Uppdraget ska genomföras av en konsult och uppdragets omfattning är 60-100 % av en heltidstjänst. Offererad konsult ska uppfylla följande obligatoriska krav: Relevant IT-utbildning, Dokumenterad erfarenhet av att beskriva och dokumentera lösningar, Dokumenterad erfarenhet av test av applikationer, Dokumenterad erfarenhet av arbete med vårdrelaterade IT-system. Har arbetat och verkat hos en Region. Då uppdraget kommer att innebära mycket kontakt med medarbetare inom Region Jönköpings län kommer stor vikt att läggas vid personlig lämplighet. Offererad konsult ska uppfylla följande krav: Är drivande och har förmåga att hålla ihop mindre uppdrag, Är strukturerad med god analytisk- och problemlösningsförmåga, God kommunikativ förmåga, Social och har lätt för att arbeta i team, Förmåga att omsätta krav i realiserbart IT-stöd, God förståelse för hela systemutvecklingskedjan, Meriterande kompetens: Dokumenterad erfarenhet av arbete inom förvaltningsområdet eHälsa hos Region Jönköpings län och då några av följande system; Blå appen, Carelink, Checkware, Journalia, Medidoc, Obstetrix, Picsara.",
                u1,
                "Skapa en professionell jobbannons i HTML med branchrelevant styling.",
                "<!DOCTYPE html><html><head><style>body {font-family: Arial, sans-serif;}h1,h2,h3 {color: #006400;}.requirements {color: #8b0000;font-weight: bold;}.merits {color: #00008b;}</style></head><body><h1>Applikationsspecialist, SwCG</h1><h2>Plats: Jönköping</h2><p>Vi söker en driven Applikationsspecialist till vår kund i Jönköping. Dennaroll erbjuder möjlighet att utvecklas inom olika verksamhetsområden ochsystemstöd inom sjukvården. Som Applikationsspecialist är det ditt uppdragatt stödja våra kunder med felsökning, problemlösning ochsystemförvaltning. Du blir länken mellan slutanvändare, systemförvaltareoch oss på IT-drift.</p><h3>Om jobbet</h3><p>I större projekt kan du komma att delta och ha ett mer kravorienteratfokus. Du får även möjlighet att bidra till att projektet levererarnödvändiga leverabler som systemdokumentation till organisationen. Vijobbar agilt och målfokuserat tillsammans och du kommer att samarbeta tättmed teamets övriga kompetenser och andra förvaltningsgrupper inomIT-centrum.</p><h3>Uppdragets omfattning</h3><p>Uppdraget ska genomföras av en konsult och uppdragets omfattning är 60-100% av en heltidstjänst.</p><h3>Krav</h3><ul class=\"requirements\"><li>Relevant IT-utbildning</li><li>Dokumenterad erfarenhet av att beskriva och dokumentera lösningar</li><li>Dokumenterad erfarenhet av test av applikationer</li><li>Dokumenterad erfarenhet av arbete med vårdrelaterade IT-system</li><li>Har arbetat och verkat hos en Region</li><li>Drivande och har förmåga att hålla ihop mindre uppdrag</li><li>Strukturerad med god analytisk- och problemlösningsförmåga</li><li>God kommunikativ förmåga</li><li>Social och har lätt för att arbeta i team</li><li>Förmåga att omsätta krav i realiserbart IT-stöd</li><li>God förståelse för hela systemutvecklingskedjan</li></ul><h3>Meriterande kompetens</h3><ul class=\"merits\"><li>Dokumenterad erfarenhet av arbete inom förvaltningsområdet eHälsa hosRegion Jönköpings län och då några av följande system; Blå appen,Carelink, Checkware, Journalia, Medidoc, Obstetrix, Picsara</li></ul></body></html>"
        );

        this.jobRepository.save(job1);
        this.jobRepository.save(job2);
        this.jobRepository.save(job3);
        this.jobRepository.save(job4);
    }
}
