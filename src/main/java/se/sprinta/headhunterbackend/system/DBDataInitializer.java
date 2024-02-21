package se.sprinta.headhunterbackend.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.job.Job;
import se.sprinta.headhunterbackend.job.JobRepository;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

@Component
public class DBDataInitializer implements CommandLineRunner {
//public class DBDataInitializer {

    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;
    private final JobRepository jobRepository;

    public DBDataInitializer(UserService userService, JdbcTemplate jdbcTemplate, JobRepository jobRepository) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
        this.jobRepository = jobRepository;
    }

    @Override
    public void run(String... args) {
//    public void run() {

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

        String instruction = "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka.";

        Job job1 = new Job(
                null,
                "Testare till Tesla"
                , "En massa nya funktioner behöver testas. Den anställde behöver vara uthållig och driven.",
                u1,
                instruction,
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "  <head>\n" +
                        "    <style>\n" +
                        "      body {\n" +
                        "        font-family: Arial, sans-serif;\n" +
                        "        background-color: #f6f6f6;\n" +
                        "        color: #333;\n" +
                        "      }\n" +
                        "\n" +
                        "      .job-ad {\n" +
                        "        width: 60%;\n" +
                        "        margin: 0 auto;\n" +
                        "        background-color: #fff;\n" +
                        "        padding: 20px;\n" +
                        "        border: 1px solid #ccc;\n" +
                        "        border-radius: 4px;\n" +
                        "        box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.15);\n" +
                        "      }\n" +
                        "\n" +
                        "      .job-ad h2 {\n" +
                        "        color: #333;\n" +
                        "        font-size: 2em;\n" +
                        "      }\n" +
                        "\n" +
                        "      .job-ad p {\n" +
                        "        line-height: 1.5;\n" +
                        "      }\n" +
                        "\n" +
                        "      .cta {\n" +
                        "        display: inline-block;\n" +
                        "        background-color: #ff4500;\n" +
                        "        color: #fff;\n" +
                        "        padding: 10px 15px;\n" +
                        "        margin-top: 20px;\n" +
                        "        border: none;\n" +
                        "        border-radius: 4px;\n" +
                        "        cursor: pointer;\n" +
                        "        text-decoration: none;\n" +
                        "      }\n" +
                        "\n" +
                        "      .cta:hover {\n" +
                        "        background-color: #ff5500;\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <div class=\"job-ad\">\n" +
                        "      <h2>Testare till Tesla</h2>\n" +
                        "      <p>\n" +
                        "        Vi letar efter en uthållig och driven testare för att hjälpa oss\n" +
                        "        verifiera våra nya, spännande funktioner.\n" +
                        "      </p>\n" +
                        "      <p>\n" +
                        "        De ansvar som medföljer rollen innefattar att noggrant utföra olika\n" +
                        "        tester för att säkerställa produktens prestanda, effektivitet och\n" +
                        "        kvalitet.\n" +
                        "      </p>\n" +
                        "      <p>\n" +
                        "        Om du är en passionerad problemlösare som älskar en utmaning, vill vi\n" +
                        "        gärna höra från dig.\n" +
                        "      </p>\n" +
                        "      <a href=\"apply.html\" class=\"cta\"> Ansök nu </a>\n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>\n");
        Job job2 = new Job(
                null,
                "AI-utvecklare till GPTPilot."
                , "Hög analytisk förmåga. Doktorand i matematisk statistik. Mångårig erfarenhet av AI-system.",
                u1,
                instruction,
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<style>\n" +
                        "body {\n" +
                        "    font-family: Verdana, sans-serif;\n" +
                        "    margin: 0;\n" +
                        "    padding: 0;\n" +
                        "    background-color: #f5f5f5;\n" +
                        "}\n" +
                        "\n" +
                        ".job-description {\n" +
                        "    margin: 20px auto;\n" +
                        "    width: 80%;\n" +
                        "    padding: 20px;\n" +
                        "    background-color: #fff;\n" +
                        "    border-radius: 10px;\n" +
                        "    box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                        "}\n" +
                        "\n" +
                        "h1, h2 {\n" +
                        "    color: #333;\n" +
                        "}\n" +
                        "\n" +
                        "h1 {\n" +
                        "    font-size: 32px;\n" +
                        "}\n" +
                        "\n" +
                        "h2 {\n" +
                        "    font-size: 24px;\n" +
                        "}\n" +
                        "\n" +
                        "p {\n" +
                        "    color: #666;\n" +
                        "    line-height: 1.6;\n" +
                        "}\n" +
                        "\n" +
                        "button {\n" +
                        "    display: inline-block;\n" +
                        "    background-color: #008CBA;\n" +
                        "    color: white;\n" +
                        "    padding: 14px 20px;\n" +
                        "    margin: 10px 0;\n" +
                        "    border: none;\n" +
                        "    border-radius: 4px;\n" +
                        "    cursor: pointer;\n" +
                        "}\n" +
                        "\n" +
                        "button:hover {\n" +
                        "    background-color: #007B9F;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<div class=\"job-description\">\n" +
                        "<h1>AI-utvecklare till GPTPilot</h1>\n" +
                        "\n" +
                        "<p>GPTPilot, en framstående aktör inom teknologi och AI-lösningar, söker nu en passionerad AI-utvecklare med stark analytisk förmåga för att bidra till vår fortsatta framgång. Om du har en doktorand i matematisk statistik och betydande erfarenhet av AI-system, vill vi gärna höra från dig.</p>\n" +
                        "\n" +
                        "<h2>Om tjänsten</h2>\n" +
                        "\n" +
                        "<p>I din roll som AI-utvecklare kommer du att bidra till att skapa och förbättra våra AI-drivna lösningar. Detta innebär att du kommer att arbeta nära vårt utvecklingsteam och hjälpa till att omvandla komplexa statistiska modeller till praktiska, förståeliga AI-lösningar.</p>\n" +
                        "\n" +
                        "<h2>De nödvändiga kvalifikationerna:</h2>\n" +
                        "\n" +
                        "<ul>\n" +
                        "    <li>Doktorand i matematisk statistik eller liknande disciplin</li>\n" +
                        "    <li>Mångårig erfarenhet av att arbeta med AI-system</li>\n" +
                        "    <li>Djupgående kunskap inom maskininlärning och programmeringsspråk som Python och R</li>\n" +
                        "    <li>Utmärkt kommunikationsförmåga och förmåga att presentera komplex information på ett klart och förståeligt sätt</li>\n" +
                        "</ul>\n" +
                        "\n" +
                        "<h2>Vad vi erbjuder</h2>\n" +
                        "\n" +
                        "<p>Vi erbjuder en konkurrenskraftig lön och omfattande förmåner, inklusive flexibla arbetstider och möjligheter för personlig och professionell utveckling. Vi är stolta över vårt stödjande och dynamiska arbetsklimat där din talang uppskattas och ditt bidrag erkänns.</p>\n" +
                        "\n" +
                        "<button>Ansök nu</button>\n" +
                        "\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>");
        Job job3 = new Job(
                null, "Programledare IT Program-/Projektledare",
                " Vi söker en programledare för att leda och styra projektresurser vars focus är att bidra till den totala leveransen inom ett mer omfattande IT-program.\\Som programledare förväntas du hantera och facilitera externa intressenter som har beroenden till programmet.Rollen kräver en kombination av teknisk expertis, dokumenterad projektledningsförmåga av större dignitet och ett tydligt affärsmannaskap.Du kommer övervaka planering, genomförande och leverans för att med intressenter, teammedlemmar och ledare säkerställa att programmet når sina mål.Programmet syftar till att ta in en ny kund till Soltak och att få alla delarna i den totala IT-leveransen på plats.\\Flertalet projekt är redan uppstartade och det finns även delar som ännu inte är uppstartade.Rollen behöver tillsättas omgående och initialt upptar programledarrollen 50% av en FTE.Möjlighet till utökande av projektledning av ett eller flera andra projekt.",
                u1,
                instruction,
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;}\n" +
                        "            \n" +
                        "        .job-details {\n" +
                        "            color: #333;\n" +
                        "            margin-bottom: 20px;}\n" +
                        "            \n" +
                        "        h1 {\n" +
                        "            color: #444;}\n" +
                        "            \n" +
                        "        .apply-btn {\n" +
                        "            background-color: #668;\n" +
                        "            color: white;\n" +
                        "            padding: 10px 20px;\n" +
                        "            text-align: center;\n" +
                        "            text-decoration: none;\n" +
                        "            display: inline-block;}\n" +
                        "            \n" +
                        "        .apply-btn:hover {\n" +
                        "            background-color: #888;\n" +
                        "            cursor: pointer;}\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <h1>Programledare IT Program-/Projektledare hos Soltak</h1>\n" +
                        "\n" +
                        "    <p>\n" +
                        "        Soltak är ett innovativt företag som strävar efter att erbjuda sina kunder bästa möjliga IT-lösningar. Vi söker nu en erfaren programledare för att stödja oss i vår fortsatta tillväxt.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <div class=\"job-details\">\n" +
                        "\n" +
                        "        <h2>Om rollen</h2>\n" +
                        "\n" +
                        "        <p>\n" +
                        "            Som programledare hos Soltak kommer du att leda och styra projektresurser vars fokus är att bidra till den totala leveransen inom ett omfattande IT-program. Du förväntas hantera och facilitera externa intressenter, samt se till att programmet når sina mål. Rollen kräver teknisk expertis, dokumenterad projektledningserfarenhet och starka affärsfärdigheter.\n" +
                        "        </p>\n" +
                        "\n" +
                        "        <h2>Dina huvudsakliga ansvarsområden</h2>\n" +
                        "\n" +
                        "        <ul>\n" +
                        "            <li>Hantering och facilitering av externa intressenter.</li>\n" +
                        "            <li>Övervakning av planering, genomförande och leverans.</li>\n" +
                        "            <li>Samarbetar med intressenter, teammedlemmar och ledare för att säkerställa att programmet når sina mål.</li>\n" +
                        "        </ul>\n" +
                        "\n" +
                        "        <h2>Vem vi letar efter</h2>\n" +
                        "\n" +
                        "        <p>\n" +
                        "            Vi söker en kandidat med kombinationen av teknisk expertis och dokumenterad projektledningsförmåga av större dignitet.\n" +
                        "        </p>\n" +
                        "\n" +
                        "        <h2>Vilka förmåner vi erbjuder</h2>\n" +
                        "\n" +
                        "        <p>\n" +
                        "            Soltak erbjuder en konkurrenskraftig lön och förmåner, samt möjligheten att arbeta i ett dynamiskt och växande företag med stora möjligheter till karriärväxt.\n" +
                        "        </p>\n" +
                        "\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <a href=\"mailto:soltakcareers@soltak.com\" class=\"apply-btn\">Ansök nu</a>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>"
        );
        Job job4 = new Job(
                null, "Applikationsspecialist",
                "SwCG söker en Applikationsspecialist till kund i Jönköping. Applikationsspecialist i förvaltningsgrupp eHälsa är en mångfacetterad roll med olika beröringspunkter inom systemförvaltning och verksamhetsutveckling. Du får möjlighet att förkovra dig inom sjukvårdens olika verksamhetsområden och dess systemstöd, leda uppdrag och stötta våra kunder med felsökning och problemlösning. Applikationsspecialisten stöttar även verksamheten kring systemförvaltning och är en länk mellan slutanvändare, systemförvaltare och oss på IT-drift. I större projekt kan du komma att delta och ha ett mer kravorienterat fokus och vara med kring anpassning av lösningen, samt bidra att projektet levererar nödvändiga leverabler som till exempel systemdokumentation till förvaltningsorganisationen. Vi jobbar agilt och målfokuserat tillsammans och du samarbetar tätt med teamets övriga kompetenser och andra förvaltningsgrupper inom IT-centrum, för att leverera väl fungerande tjänster och god service till våra kunder på sjukhusen och andra vårdgivare i länet. Uppdraget ska genomföras av en konsult och uppdragets omfattning är 60-100 % av en heltidstjänst. Offererad konsult ska uppfylla följande obligatoriska krav: Relevant IT-utbildning, Dokumenterad erfarenhet av att beskriva och dokumentera lösningar, Dokumenterad erfarenhet av test av applikationer, Dokumenterad erfarenhet av arbete med vårdrelaterade IT-system. Har arbetat och verkat hos en Region. Då uppdraget kommer att innebära mycket kontakt med medarbetare inom Region Jönköpings län kommer stor vikt att läggas vid personlig lämplighet. Offererad konsult ska uppfylla följande krav: Är drivande och har förmåga att hålla ihop mindre uppdrag, Är strukturerad med god analytisk- och problemlösningsförmåga, God kommunikativ förmåga, Social och har lätt för att arbeta i team, Förmåga att omsätta krav i realiserbart IT-stöd, God förståelse för hela systemutvecklingskedjan, Meriterande kompetens: Dokumenterad erfarenhet av arbete inom förvaltningsområdet eHälsa hos Region Jönköpings län och då några av följande system; Blå appen, Carelink, Checkware, Journalia, Medidoc, Obstetrix, Picsara.",
                u1,
                instruction,
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Jobbannons: Applikationsspecialist på SwCG</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "        }\n" +
                        "        .jobs {\n" +
                        "            margin: 0 auto;\n" +
                        "            width: 80%;\n" +
                        "        }\n" +
                        "        h1, h2 {\n" +
                        "            color: #333;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            line-height: 1.6;\n" +
                        "        }\n" +
                        "        .apply-button {\n" +
                        "            display: inline-block;\n" +
                        "            margin-top: 20px;\n" +
                        "            padding: 10px 20px;\n" +
                        "            background-color: #008CBA;\n" +
                        "            text-align: center;\n" +
                        "            color: white;\n" +
                        "            cursor: pointer;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"jobs\">\n" +
                        "        <h1>SwCG söker en Applikationsspecialist till kund i Jönköping</h1>\n" +
                        "        <p>Vi på SwCG söker nu en dedikerad och duktig Applikationsspecialist till förvaltningsgrupp eHälsa. Hos oss kommer du att få chansen att förkovra dig inom sjukvårdens olika verksamhetsområden och dess systemstöd, leda uppdrag och stötta våra kunder med felsökning och problemlösning. Vi söker dig som har relevant IT-utbildning och har dokumenterad erfarenhet av att beskriva och dokumentera lösningar, test av applikationer och arbete med vårdrelaterade IT-system.</p>\n" +
                        "        <h2>Arbetsuppgifter</h2>\n" +
                        "        <p>Som Applikationsspecialist kommer du att stödja verksamheten kring systemförvaltning och agera som en länk mellan slutanvändare, systemförvaltare och oss på IT-drift. I större projekt kan du komma att delta och ha ett mer kravorienterat fokus och vara med kring anpassning av lösningen, samt bidra att projektet levererar nödvändiga leverabler till förvaltningsorganisationen.</p>\n" +
                        "        <h2>Kvalifikationer</h2>\n" +
                        "        <ul>\n" +
                        "            <li>Relevant IT-utbildning</li>\n" +
                        "            <li>Dokumenterad erfarenhet av att beskriva och dokumentera lösningar</li>\n" +
                        "            <li>Dokumenterad erfarenhet av test av applikationer</li>\n" +
                        "            <li>Dokumenterad erfarenhet av arbete med vårdrelaterade IT-system</li>\n" +
                        "            <li>Har arbetat och verkat hos en Region</li>\n" +
                        "        </ul>\n" +
                        "        <h2>Personliga egenskaper</h2>\n" +
                        "        <p>Vi lägger stor vikt vid dina personliga egenskaper. Du ska vara drivande och ha förmåga att hålla ihop mindre uppdrag. Du bör även vara strukturerad med god analytisk- och problemlösningsförmåga och ha mycket god kommunikativ förmåga. Du är social och har lätt för att arbeta i team. Det är även mycket meriterande om du har dokumenterad erfarenhet av arbete inom förvaltningsområdet eHälsa hos Region Jönköpings län och då några av följande system; Blå appen, Carelink, Checkware, Journalia, Medidoc, Obstetrix, Picsara.</p>\n" +
                        "        <a href=\"https://www.sis.se/karriar/lediga-jobb\" class=\"apply-button\">Ansök nu!</a>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>"
        );

        Job job5 = new Job(
                null,
                "Utvecklare AI-app",
                "En applikation för AI-genererade jobbannonser. Frontend är skriven i React, och tillhörande libraries är React Router, DOMPurify, CSS och Styled Components. Backend är skriven i Java och Spring Boot, Spring Security samt kommunikation med ett AI API används. Databasen sköts av MySQL. Applikationen styrs genom molntjänsten Azure. GIT och GitHub används som versionshanterare. Alla inblandade behöver kontinuerligt vara beredda på att sätta sig in i nya libraries och frameworks. Målet är att applikationen ska sjösättas inom 1 år. Applikationen växer snabbt och de nuvarande två utvecklarna får allt svårare att hinna med allt som behöver göras. Ytterligare en utvecklare behövs nu.",
                u1,
                instruction,
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Job Announcement</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "        }\n" +
                        "\n" +
                        "        .container {\n" +
                        "            width: 80%;\n" +
                        "            margin: auto;\n" +
                        "            overflow: hidden;\n" +
                        "        }\n" +
                        "\n" +
                        "        .main-header {\n" +
                        "            background-color: #444;\n" +
                        "            color: #fff;\n" +
                        "            height: 80px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .main-footer {\n" +
                        "            background-color: #444;\n" +
                        "            color: #fff;\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px;\n" +
                        "            margin-top: 40px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .job h1 {\n" +
                        "            color: #444;\n" +
                        "        }\n" +
                        "\n" +
                        "        .job p {\n" +
                        "            font-size: 16px;\n" +
                        "            color: #666;\n" +
                        "        }\n" +
                        "\n" +
                        "        .job-details {\n" +
                        "            background-color: #fff;\n" +
                        "            padding: 20px;\n" +
                        "            border-radius: 5px;\n" +
                        "            margin-top: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .apply-button {\n" +
                        "            background-color: #5cb85c;\n" +
                        "            color: #fff;\n" +
                        "            padding: 10px 30px;\n" +
                        "            text-decoration: none;\n" +
                        "            border-radius: 5px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .apply-button:hover {\n" +
                        "            background-color: #449d44;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <header class=\"main-header\">\n" +
                        "        <div class=\"container\">\n" +
                        "            <h1 class=\"main-title\">Job Opportunity: Software Developer</h1>\n" +
                        "        </div>\n" +
                        "    </header>\n" +
                        "\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"job\">\n" +
                        "            <h1>Welcome to Our Team!</h1>\n" +
                        "            <p>\n" +
                        "                We are developing an innovative application for AI-generated job ads. Our frontend is powered by React and uses React Router, DOMPurify, CSS and Styled Components. The backend is driven by Java and Spring Boot, collaborating with an AI API. Our data is managed in MySQL and the application is governed through Azure cloud service. We utilize GIT and GitHub for version control. The project is growing rapidly, and we are in need of an extra hand to keep up with all the tasks.\n" +
                        "            </p>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"job-details\">\n" +
                        "            <h2>Responsibilities</h2>\n" +
                        "            <p>\n" +
                        "                As a software developer on the team, you'll participate in full life cycle application development from design to deployment. You'll write clean, efficient code and maintain high software quality through extensive testing. You'll also assist your team members in preparatory research on new libraries and frameworks and help adapt our system to them accordingly.\n" +
                        "            </p>\n" +
                        "\n" +
                        "            <h2>Qualifications</h2>\n" +
                        "            <p>\n" +
                        "                Good experience of using React, Java, Spring Boot, and MySQL.\n" +
                        "                Familiarity with Azure cloud service is a plus.\n" +
                        "                Experience using GIT and GitHub for version control.\n" +
                        "                Must be flexible and eager to learn new libraries and frameworks.\n" +
                        "            </p>\n" +
                        "\n" +
                        "            <h2>Benefits</h2>\n" +
                        "            <p>\n" +
                        "                We value our team members and do everything we can to ensure they lead a balanced life. Our comprehensive benefits include medical coverage, professional growth opportunities, flexible work hours, and remote work options.\n" +
                        "            </p>\n" +
                        "\n" +
                        "            <a class=\"apply-button\" href=\"#\">Apply Now</a>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <footer class=\"main-footer\">\n" +
                        "        <div class=\"container\">\n" +
                        "            <p class=\"footer-text\">\n" +
                        "                If you need assistance during the application process, please contact our HR team at hr@company.com.\n" +
                        "            </p>\n" +
                        "        </div>\n" +
                        "    </footer>\n" +
                        "</body>\n" +
                        "</html>"
        );

        this.jobRepository.save(job1);
        this.jobRepository.save(job2);
        this.jobRepository.save(job3);
        this.jobRepository.save(job4);
//        this.jobRepository.save(job5);
    }
}
