package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Job API endpoints")
@ActiveProfiles("test-mysql")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class JobControllerAuthorityIntegrationMySQLTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Value("${api.endpoint.base-url-account}")
    private String baseUrlAccount;

    @Value("${api.endpoint.base-url-job}")
    private String baseUrlJob;

    public String userToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlAccount + "/login")
                        .with(httpBasic(
                                "user1-mysql@hh.se",
                                "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        return "Bearer " + json.getJSONObject("data").getString("token");
    }

    public String adminToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                post(this.baseUrlAccount + "/login")
                        .with(httpBasic("admin-mysql@hh.se",
                                "a"))); // httpBasic() is from spring-security-test.
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        return "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("(GET) findAll - Admin Permission - Success")
    void test_FindAll_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data[0].description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data[0].instruction").value("job1 Instruction 1"))
                .andExpect(jsonPath("$.data[0].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[0].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[0].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[0].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[0].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("job2 Title 2"))
                .andExpect(jsonPath("$.data[1].description").value("job2 Description 2"))
                .andExpect(jsonPath("$.data[1].instruction").value("job2 Instruction 2"))
                .andExpect(jsonPath("$.data[1].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[1].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[1].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[1].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[1].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[2].id").value(3L))
                .andExpect(jsonPath("$.data[2].title").value("job3 Title 3"))
                .andExpect(jsonPath("$.data[2].description").value("job3 Description 3"))
                .andExpect(jsonPath("$.data[2].instruction").value("job3 Instruction 3")).andExpect(jsonPath("$.data[0].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[2].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[2].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[2].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[2].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[3].id").value(4L))
                .andExpect(jsonPath("$.data[3].title").value("Fullstack Utvecklare"))
                .andExpect(jsonPath("$.data[3].description").value("Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år."))
                .andExpect(jsonPath("$.data[3].instruction").value("Du ska skapa en jobbannons på svenska i HTML-format med en professionell CSS styling. För att omarbeta en arbetsbeskrivning till en jobbannons, börja med att läsa igenom arbetsbeskrivningen noggrant för att förstå de huvudsakliga arbetsuppgifterna, nödvändiga kompetenser och kvalifikationer. Sedan, översätt denna information till en mer engagerande och tilltalande form som lockar potentiella kandidater. Det är viktigt att framhäva företagets kultur och de unika fördelarna med att arbeta där. Börja annonsen med en kort introduktion till företaget, följt av en översikt av jobbrollen. Använd en positiv och inkluderande ton, och undvik jargong. Gör klart vilka huvudsakliga ansvarsområden rollen innefattar och vilka färdigheter och erfarenheter som är önskvärda. Inkludera även information om eventuella förmåner eller möjligheter till personlig och professionell utveckling. Avsluta med hur man ansöker till tjänsten, inklusive viktiga datum och kontaktinformation. Kom ihåg att vara tydlig och koncis för att hålla potentiella kandidaters uppmärksamhet. En välformulerad jobbannons ska inte bara informera utan också inspirera och locka rätt talanger till att söka."))
                .andExpect(jsonPath("$.data[3].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[3].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[3].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[3].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[3].applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) findAll - User No Permission - Exception")
    void test_FindAll_UserNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/findAll")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("(GET) getAllJobDtos - Admin Permission - Success")
    void test_GetAllJobDtos_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/getAllJobDtos")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data[0].description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data[0].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[0].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[0].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[0].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[0].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[1].title").value("job2 Title 2"))
                .andExpect(jsonPath("$.data[1].description").value("job2 Description 2"))
                .andExpect(jsonPath("$.data[1].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[1].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[1].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[1].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[1].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[2].title").value("job3 Title 3"))
                .andExpect(jsonPath("$.data[2].description").value("job3 Description 3"))
                .andExpect(jsonPath("$.data[2].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[2].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[2].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[2].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[3].title").value("Fullstack Utvecklare"))
                .andExpect(jsonPath("$.data[3].description").value("Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år."))
                .andExpect(jsonPath("$.data[3].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[3].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[3].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[3].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[3].applicationDeadline").isEmpty());

    }

    @Test
    @DisplayName("(GET) getAllJobDtos - User Permission - Success")
    void test_GetAllJobDtos_UserPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/getAllJobDtos")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.data[0].title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data[0].description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data[0].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[0].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[0].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[0].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[0].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[1].title").value("job2 Title 2"))
                .andExpect(jsonPath("$.data[1].description").value("job2 Description 2"))
                .andExpect(jsonPath("$.data[1].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[1].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[1].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[1].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[1].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[2].title").value("job3 Title 3"))
                .andExpect(jsonPath("$.data[2].description").value("job3 Description 3"))
                .andExpect(jsonPath("$.data[2].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[2].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[2].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[2].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[3].title").value("Fullstack Utvecklare"))
                .andExpect(jsonPath("$.data[3].description").value("Tjänsten omfattar en utvecklare som behärskar frontend, backend och databashantering. I frontend används React för att skapa en interaktiv web applikation. Användaren lotsas runt med hjälp av React Router. Även DOMPurify, Bootstrap 5, CSS och Styled Components används för att lösa olika utmaningar. I backend används Java, Spring Boot, Spring Security och en koppling mot ett AI API. Databasen hanteras av MySQL. Azure används som molnplattform för projektet. Utvecklaren arbetar både indivuduellt och i tillsammans med teamet. Nya libraries och frameworks kan komma att introduceras. Projektet beräknas ha passerat utvecklingsfasen om 2 år."))
                .andExpect(jsonPath("$.data[3].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[3].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[3].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[3].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[3].applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) getAllJobDtosByUserEmail - Admin Permission - Success")
    void test_getAllJobDtosByUserEmail_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/getAllJobDtosByUserEmail" + "/user1-mysql@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Jobs Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data[0].description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data[0].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[0].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[0].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[0].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[0].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[1].title").value("job2 Title 2"))
                .andExpect(jsonPath("$.data[1].description").value("job2 Description 2"))
                .andExpect(jsonPath("$.data[1].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[1].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[1].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[1].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[1].applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) getAllJobDtosByUserEmail - Admin Permission - Invalid Email - Exception)")
    void test_getAllJobDtosByUserEmail_AdminPermission_InvalidEmail_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/getAllJobDtosByUserEmail" + "/Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(GET) getAllJobDtosByUserEmail - User Permission - Success")
    void test_getAllJobDtosByUserEmail_UserPermission_Success() throws Exception {

        this.mockMvc.perform(get(this.baseUrlJob + "/getAllJobDtosByUserEmail" + "/user1-mysql@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User Jobs Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data[0].description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data[0].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[0].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[0].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[0].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[0].applicationDeadline").isEmpty())
                .andExpect(jsonPath("$.data[1].title").value("job2 Title 2"))
                .andExpect(jsonPath("$.data[1].description").value("job2 Description 2"))
                .andExpect(jsonPath("$.data[1].recruiterName").isEmpty())
                .andExpect(jsonPath("$.data[1].adCompany").isEmpty())
                .andExpect(jsonPath("$.data[1].adEmail").isEmpty())
                .andExpect(jsonPath("$.data[1].adPhone").isEmpty())
                .andExpect(jsonPath("$.data[1].applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) getAllJobDtosByUserEmail - User Permission - Invalid Email - Exception")
    void test_getAllJobDtosByUserEmail_UserPermission_InvalidEmail_Exception() throws Exception {

        this.mockMvc.perform(get(this.baseUrlJob + "/getAllJobDtosByUserEmail" + "/Invalid Email")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(GET) findById - Admin Permission - Success")
    void test_findById_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/findById" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data.description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data.instruction").value("job1 Instruction 1"))
                .andExpect(jsonPath("$.data.recruiterName").isEmpty())
                .andExpect(jsonPath("$.data.adCompany").isEmpty())
                .andExpect(jsonPath("$.data.adEmail").isEmpty())
                .andExpect(jsonPath("$.data.adPhone").isEmpty())
                .andExpect(jsonPath("$.data.applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) findById - Admin Permission - Invalid Id - Exception")
    void test_findById_AdminPermission_InvalidId_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/findById" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(GET) findById - User No Permission - Exception")
    void test_findById_UserNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/findById" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("(GET) getJobById - Admin Permission - Success")
    void test_GetJobById_AdminPermission_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/getJobById" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data.description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data.recruiterName").isEmpty())
                .andExpect(jsonPath("$.data.adCompany").isEmpty())
                .andExpect(jsonPath("$.data.adEmail").isEmpty())
                .andExpect(jsonPath("$.data.adPhone").isEmpty())
                .andExpect(jsonPath("$.data.applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) getJobById - Admin Permission - Invalid Id - Exception")
    void test_GetJobById_AdminPermission_InvalidId_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/getJobById" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + +Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(GET) getJobById - User Permission - Success")
    void test_GetJobById_UserPermission_Success() throws Exception {

        this.mockMvc.perform(get(this.baseUrlJob + "/getJobById" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.title").value("job1 Title 1"))
                .andExpect(jsonPath("$.data.description").value("job1 Description 1"))
                .andExpect(jsonPath("$.data.recruiterName").isEmpty())
                .andExpect(jsonPath("$.data.adCompany").isEmpty())
                .andExpect(jsonPath("$.data.adEmail").isEmpty())
                .andExpect(jsonPath("$.data.adPhone").isEmpty())
                .andExpect(jsonPath("$.data.applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(GET) getJobById - User Permission - Invalid Id - Exception")
    void test_GetJobById_UserPermission_InvalidId_Exception() throws Exception {

        this.mockMvc.perform(get(this.baseUrlJob + "/getJobById" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + +Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(POST) addJob - Admin No Permission - (Exception)")
    void test_AddJob_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(post(this.baseUrlJob + "/addJob" + "/user1-mysql@hh.se")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("(POST) addJob - User Permission - Success")
    void test_AddJob_UserPermission_Success() throws Exception {
        JobDtoFormAdd newJob = new JobDtoFormAdd(
                "New Job, Title",
                "New Job, Description",
                "New Job, Instruction");

        String json = this.objectMapper.writeValueAsString(newJob);

        this.mockMvc.perform(post(this.baseUrlJob + "/addJob" + "/user1-mysql@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.title").value("New Job, Title"))
                .andExpect(jsonPath("$.data.description").value("New Job, Description"))
                .andExpect(jsonPath("$.data.recruiterName").isEmpty())
                .andExpect(jsonPath("$.data.adCompany").isEmpty())
                .andExpect(jsonPath("$.data.adEmail").isEmpty())
                .andExpect(jsonPath("$.data.adPhone").isEmpty())
                .andExpect(jsonPath("$.data.applicationDeadline").isEmpty());
    }

    @Test
    @DisplayName("(POST) addJob - User Permission - Invalid Email - Exception")
    void test_AddJob_UserPermission_InvalidEmail_Exception() throws Exception {

        JobDtoFormAdd newJob = new JobDtoFormAdd(
                "New Job, Title",
                "New Job, Description",
                "New Job, Instruction");

        String json = this.objectMapper.writeValueAsString(newJob);

        this.mockMvc.perform(post(this.baseUrlJob + "/addJob" + "/Invalid Email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(POST) addJob - User Permission - Invalid Form - Exception")
    void test_AddJob_UserPermission_InvalidForm_Exception() throws Exception {

        JobDtoFormAdd newJob = new JobDtoFormAdd(null, null, null);

        String json = this.objectMapper.writeValueAsString(newJob);

        this.mockMvc.perform(post(this.baseUrlJob + "/addJob" + "/user1-mysql@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.description").value("Description is required"));
    }

    @Test
    @DisplayName("(PUT) update - Admin No Permission - Exception")
    void test_Update_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(put(this.baseUrlJob + "/update" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("(PUT) update - User Permission - Success")
    void test_Update_UserPermission_Success() throws Exception {
        JobDtoFormUpdate updateJob = new JobDtoFormUpdate(
                "Updated Title",
                "Updated Description",
                "Updated Instruction",
                "Updated recruiterName",
                "Updated adCompany",
                "Updated adEmail",
                "Updated adPhone",
                "Updated applicationDeadline"
        );

        String json = this.objectMapper.writeValueAsString(updateJob);

        this.mockMvc.perform(put(this.baseUrlJob + "/update" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.title").value("Updated Title"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"))
                .andExpect(jsonPath("$.data.recruiterName").value("Updated recruiterName"))
                .andExpect(jsonPath("$.data.adCompany").value("Updated adCompany"))
                .andExpect(jsonPath("$.data.adEmail").value("Updated adEmail"))
                .andExpect(jsonPath("$.data.adPhone").value("Updated adPhone"))
                .andExpect(jsonPath("$.data.applicationDeadline").value("Updated applicationDeadline"));
    }

    @Test
    @DisplayName("(PUT) update - User Permission - Invalid Id - Exception")
    void test_Update_UserPermission_InvalidId_Exception() throws Exception {
        JobDtoFormUpdate updateJob = new JobDtoFormUpdate(
                "Updated Title",
                "Updated Description",
                "Updated Instruction",
                "Updated recruiterName",
                "Updated adCompnany",
                "Updated adEmail",
                "Updated adPhone",
                "Updated applicationDeadline"
        );

        String json = this.objectMapper.writeValueAsString(updateJob);

        this.mockMvc.perform(put(this.baseUrlJob + "/update" + "/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(PUT) update - User Permission - Invalid Form - Exception")
    void test_Update_UserPermission_InvalidForm_Exception() throws Exception {

        JobDtoFormUpdate newJob = new JobDtoFormUpdate(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "");

        String json = this.objectMapper.writeValueAsString(newJob);

        this.mockMvc.perform(put(this.baseUrlJob + "/update" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.description").value("Description is required"));
    }

    @Test
    @DisplayName("(DELETE) delete - Admin No Permission - Exception")
    void test_Delete_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/user1-mysql@hh.se" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("(DELETE) delete - User Permission - Success")
    void test_Delete_UserPermission_Success() throws Exception {

        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/user1-mysql@hh.se" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    @Test
    @DisplayName("(DELETE) delete - Invalid Email - Exception")
    void test_Delete_InvalidEmail_Exception() throws Exception {

        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/Invalid Email" + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email Invalid Email"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(DELETE) delete - Invalid Id - Exception")
    void test_Delete_InvalidId_Exception() throws Exception {

        this.mockMvc.perform(delete(this.baseUrlJob + "/delete" + "/user1-mysql@hh.se" + "/" + Long.MAX_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + Long.MAX_VALUE))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("(GET) generate - Admin No Permission (Exception)")
    void test_Generate_AdminNoPermission_Exception() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/generate" + "/4")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, adminToken()))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("No permission"))
                .andExpect(jsonPath("$.data").value("Access Denied"));
    }

    @Test
    @DisplayName("(GET) generate - User Permission - Success")
    @Disabled
    void test_Generate_Success() throws Exception {
        this.mockMvc.perform(get(this.baseUrlJob + "/generate" + "/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, userToken()))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Generate Ad Success"));
    }
}
