package se.sprinta.headhunterbackend.client.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Choice;
import se.sprinta.headhunterbackend.client.chat.dto.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ActiveProfiles("mock-test")
@RestClientTest(OpenAiChatClient.class)
class OpenAiChatClientTest {

    @Autowired
    private OpenAiChatClient openAiChatClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    private ChatRequest chatRequest;

    @Value("${ai.openai.endpoint}")
    private String url;

    @Value("${ai.openai.model}")
    private String model;

    @BeforeEach
    void setUp() {
        this.chatRequest = new ChatRequest(this.model, List.of(
                new Message("system", "Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka."),
                new Message("user", "Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år.")
        ));
    }

    @Test
    void testGenerateSuccess() throws JsonProcessingException {
        // Given
        ChatResponse chatResponse = new ChatResponse(List.of(
                new Choice(0, new Message("assistant", """
                        <!DOCTYPE html>
                        <html lang="sv">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Jobbannons - Fullstack-utvecklare</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #f4f4f4;
                                    color: #333;
                                    line-height: 1.6;
                                    margin: 0;
                                    padding: 0;
                                }
                                .container {
                                    max-width: 800px;
                                    margin: 20px auto;
                                    background: #fff;
                                    padding: 20px;
                                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                }
                                h1 {
                                    color: #007BFF;
                                }
                                h2 {
                                    color: #6c757d;
                                }
                                ul {
                                    list-style: none;
                                    padding: 0;
                                }
                                li {
                                    margin-bottom: 10px;
                                }
                                .btn-apply {
                                    display: inline-block;
                                    font-size: 18px;
                                    color: #fff;
                                    background-color: #007BFF;
                                    padding: 10px 20px;
                                    text-decoration: none;
                                    border-radius: 5px;
                                }
                                .btn-apply:hover {
                                    background-color: #0056b3;
                                }
                                .footer {
                                    text-align: center;
                                    margin-top: 20px;
                                    color: #6c757d;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <h1>Fullstack-utvecklare</h1>
                                <p>Är du en driven utvecklare som är lika skicklig på frontend som backend och databashantering? Trivs du med att arbeta både självständigt och tillsammans med ett team för att skapa innovativa lösningar? Då kan du vara den vi söker!</p>
                               \s
                                <h2>Om företaget</h2>
                                <p>Vi är ett dynamiskt och framtidsinriktat företag som satsar på att utveckla nästa generations webbapplikationer. Med en stark företagskultur som bygger på innovation, samarbete och personlig utveckling, erbjuder vi en inspirerande arbetsmiljö där du har möjlighet att växa och utvecklas.</p>
                               \s
                                <h2>Om rollen</h2>
                                <p>Som fullstack-utvecklare hos oss kommer du att arbeta med:</p>
                                <ul>
                                    <li>Frontend-utveckling med React, React Router, DOMPurify, Bootstrap 5, CSS och Styled Components</li>
                                    <li>Backend-utveckling med Java, Spring Boot, Spring Security och integration mot ett AI API</li>
                                    <li>Databashantering med MySQL</li>
                                    <li>Användning av Azure som molnplattform</li>
                                    <li>Arbete både individuellt och i team</li>
                                    <li>Införande av nya libraries och frameworks efter behov</li>
                                </ul>
                               \s
                                <h2>Vem är du?</h2>
                                <p>För att lyckas i den här rollen ser vi att du har:</p>
                                <ul>
                                    <li>Erfarenhet av frontend-utveckling med React och dess ekosystem</li>
                                    <li>Goda kunskaper i Java och Spring Boot</li>
                                    <li>Kompetens inom MySQL databashantering</li>
                                    <li>Erfarenhet av att arbeta med molntjänster, gärna Azure</li>
                                    <li>God förmåga att arbeta både självständigt och i team</li>
                                    <li>Viljan att lära dig nya teknologier och anpassa dig till förändringar</li>
                                </ul>
                               \s
                                <h2>Vad vi erbjuder</h2>
                                <ul>
                                    <li>En inspirerande arbetsmiljö med fokus på innovation och personlig utveckling</li>
                                    <li>Möjlighet att arbeta med senaste teknologierna och verktygen</li>
                                    <li>Stöd från ett engagerat och kunnigt team</li>
                                    <li>Flexibilitet och möjlighet att påverka ditt arbete</li>
                                    <li>Konkurrenskraftig lön och förmåner</li>
                                </ul>
                               \s
                                <h2>Ansökan</h2>
                                <p>Låter detta som din nästa utmaning? Skicka då din ansökan senast [sista ansökningsdatum] till [kontaktpersonens e-postadress]. Vi ser fram emot att höra från dig!</p>
                               \s
                                <a href="mailto:[kontaktpersonens e-postadress]?subject=Ansökan%20till%20Fullstack-utvecklare%20tjänsten" class="btn-apply">Ansök nu</a>
                               \s
                                <div class="footer">
                                    <p>För frågor om tjänsten, kontakta [kontaktpersonens namn] på [kontaktpersonens telefonnummer].</p>
                                </div>
                            </div>
                        </body>
                        </html>"""))));

        this.mockServer.expect(requestTo(this.url)) // Check whether we make a request to this url.
                .andExpect(method(HttpMethod.POST)) // Check whether this is a POST request.
                .andExpect(header("Authorization", startsWith("Bearer "))) // Check whether the header is set correctly.
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.chatRequest))) // Check whether the chatRequest is sent.
                .andRespond(withSuccess(this.objectMapper.writeValueAsString(chatResponse), MediaType.APPLICATION_JSON)); // The mock OpenAI server should respond this json message.

        // When
        ChatResponse generatedChatResponse = this.openAiChatClient.generate(this.chatRequest);

        // Then
        this.mockServer.verify(); // Verify that all expected requests set up with expect and andExpect were indeed performed
        assertThat(generatedChatResponse.choices().get(0).message().content()).isEqualTo("""
                <!DOCTYPE html>
                <html lang="sv">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Jobbannons - Fullstack-utvecklare</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            color: #333;
                            line-height: 1.6;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 800px;
                            margin: 20px auto;
                            background: #fff;
                            padding: 20px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        }
                        h1 {
                            color: #007BFF;
                        }
                        h2 {
                            color: #6c757d;
                        }
                        ul {
                            list-style: none;
                            padding: 0;
                        }
                        li {
                            margin-bottom: 10px;
                        }
                        .btn-apply {
                            display: inline-block;
                            font-size: 18px;
                            color: #fff;
                            background-color: #007BFF;
                            padding: 10px 20px;
                            text-decoration: none;
                            border-radius: 5px;
                        }
                        .btn-apply:hover {
                            background-color: #0056b3;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 20px;
                            color: #6c757d;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Fullstack-utvecklare</h1>
                        <p>Är du en driven utvecklare som är lika skicklig på frontend som backend och databashantering? Trivs du med att arbeta både självständigt och tillsammans med ett team för att skapa innovativa lösningar? Då kan du vara den vi söker!</p>
                       \s
                        <h2>Om företaget</h2>
                        <p>Vi är ett dynamiskt och framtidsinriktat företag som satsar på att utveckla nästa generations webbapplikationer. Med en stark företagskultur som bygger på innovation, samarbete och personlig utveckling, erbjuder vi en inspirerande arbetsmiljö där du har möjlighet att växa och utvecklas.</p>
                       \s
                        <h2>Om rollen</h2>
                        <p>Som fullstack-utvecklare hos oss kommer du att arbeta med:</p>
                        <ul>
                            <li>Frontend-utveckling med React, React Router, DOMPurify, Bootstrap 5, CSS och Styled Components</li>
                            <li>Backend-utveckling med Java, Spring Boot, Spring Security och integration mot ett AI API</li>
                            <li>Databashantering med MySQL</li>
                            <li>Användning av Azure som molnplattform</li>
                            <li>Arbete både individuellt och i team</li>
                            <li>Införande av nya libraries och frameworks efter behov</li>
                        </ul>
                       \s
                        <h2>Vem är du?</h2>
                        <p>För att lyckas i den här rollen ser vi att du har:</p>
                        <ul>
                            <li>Erfarenhet av frontend-utveckling med React och dess ekosystem</li>
                            <li>Goda kunskaper i Java och Spring Boot</li>
                            <li>Kompetens inom MySQL databashantering</li>
                            <li>Erfarenhet av att arbeta med molntjänster, gärna Azure</li>
                            <li>God förmåga att arbeta både självständigt och i team</li>
                            <li>Viljan att lära dig nya teknologier och anpassa dig till förändringar</li>
                        </ul>
                       \s
                        <h2>Vad vi erbjuder</h2>
                        <ul>
                            <li>En inspirerande arbetsmiljö med fokus på innovation och personlig utveckling</li>
                            <li>Möjlighet att arbeta med senaste teknologierna och verktygen</li>
                            <li>Stöd från ett engagerat och kunnigt team</li>
                            <li>Flexibilitet och möjlighet att påverka ditt arbete</li>
                            <li>Konkurrenskraftig lön och förmåner</li>
                        </ul>
                       \s
                        <h2>Ansökan</h2>
                        <p>Låter detta som din nästa utmaning? Skicka då din ansökan senast [sista ansökningsdatum] till [kontaktpersonens e-postadress]. Vi ser fram emot att höra från dig!</p>
                       \s
                        <a href="mailto:[kontaktpersonens e-postadress]?subject=Ansökan%20till%20Fullstack-utvecklare%20tjänsten" class="btn-apply">Ansök nu</a>
                       \s
                        <div class="footer">
                            <p>För frågor om tjänsten, kontakta [kontaktpersonens namn] på [kontaktpersonens telefonnummer].</p>
                        </div>
                    </div>
                </body>
                </html>""");
    }

    /**
     * This test simulates receiving a 401 Unauthorized response.
     */

    @Test
    void testGenerateUnauthorizedRequest() {
        // Given
        this.mockServer.expect(requestTo(this.url))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withUnauthorizedRequest());

        // When
        Throwable thrown = assertThrows(HttpClientErrorException.class,
                () -> this.openAiChatClient.generate(this.chatRequest));

        // Then
        this.mockServer.verify();
        assertThat(thrown)
                .isInstanceOf(HttpClientErrorException.Unauthorized.class); // All 400 level errors, including Unauthorized, are nested classes inside the HttpClientErrorException class
    }

    /**
     * This test simulates a scenario where the service receives a 429 TooManyRequests response.
     */

    @Test
    void voidGenerateQuoteExceeded() {
        // Given
        this.mockServer.expect(requestTo(this.url))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withTooManyRequests());

        // When
        Throwable thrown = assertThrows(HttpClientErrorException.class,
                () -> this.openAiChatClient.generate(this.chatRequest));

        // Then
        this.mockServer.verify();
        assertThat(thrown)
                .isInstanceOf(HttpClientErrorException.TooManyRequests.class);

    }

    /**
     * This test simulates receiving a 500 Internal Server Error response.
     */

    @Test
    void voidGenerateInternalServerError() {
        // Given
        this.mockServer.expect(requestTo(this.url))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        // When
        Throwable thrown = assertThrows(HttpServerErrorException.InternalServerError.class,
                () -> this.openAiChatClient.generate(this.chatRequest));

        // Then
        this.mockServer.verify();
        assertThat(thrown).isInstanceOf(HttpServerErrorException.InternalServerError.class);
    }

    /**
     * This test simulates receiving a 503 Service Unavailable response.
     */

    @Test
    void GenerateServiceUnavailableError() {
        // Given
        this.mockServer.expect(requestTo(this.url))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServiceUnavailable());

        // When
        Throwable thrown = assertThrows(HttpServerErrorException.ServiceUnavailable.class,
                () -> this.openAiChatClient.generate(this.chatRequest));

        // Then
        this.mockServer.verify();
        assertThat(thrown).isInstanceOf(HttpServerErrorException.ServiceUnavailable.class);
    }
}