package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoForm;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JobService jobService;

    @Autowired
    ObjectMapper objectMapper;

    List<Job> jobs;

    @Autowired
    JobToJobDtoViewConverter jobToJobDtoViewConverter;

    @Value("${api.endpoint.base-url-jobs}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("m@e.se");

        Job j1 = new Job();
        j1.setId(1L);
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        j1.setUser(user);
        Job j2 = new Job();
        j2.setId(2L);
        j2.setDescription(".Net-junior till vårt nya kontor.");
        j2.setUser(user);
        Job j3 = new Job();
        j3.setId(3L);
        j3.setDescription("HR-ninja till vår nya avdelning på Mynttorget.");
        j3.setUser(user);

        this.jobs = new ArrayList<>();

        this.jobs.add(j1);
        this.jobs.add(j2);
        this.jobs.add(j3);

        System.out.println(jobs.get(0).getId());
        System.out.println(jobs.get(0).getDescription());
        System.out.println(jobs.get(1).getId());
        System.out.println(jobs.get(1).getDescription());
        System.out.println(jobs.get(2).getId());
        System.out.println(jobs.get(2).getDescription());
    }

    @Test
    void testFindAllJobsSuccess() throws Exception {
        User user = new User();
        user.setEmail("m@e.se");

        // Given
        given(this.jobService.findAll()).willReturn(this.jobs);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].description").value("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].description").value(".Net-junior till vårt nya kontor."))
                .andExpect(jsonPath("$.data[2].id").value(3L))
                .andExpect(jsonPath("$.data[2].description").value("HR-ninja till vår nya avdelning på Mynttorget."));
    }

    @Test
    void testFindJobByIdSuccess() throws Exception {
        // Given
        given(this.jobService.findById(1L)).willReturn(this.jobs.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/findJob/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.description").value("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."));
    }

    @Test
    void testFindJobByIdWithNonExistentId() throws Exception {
        Long id = Mockito.any(Long.class); // Works just as well as Mockito.anyLong();

        // Given
        given(this.jobService.findById(id)).willThrow(new ObjectNotFoundException("job", id));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/findJob/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + id))
                .andExpect(jsonPath("$.data").isEmpty());
    }

//    @Test
//    void testUpdateJobSuccess() throws Exception {
//
//        Job newJob = new Job();
//        newJob.setDescription("Testare på Tesla.");
//
//        this.jobService.save(newJob);
//
//        JobDtoForm jobDto = new JobDtoForm(
//                "m@e.se",
//                "Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."
//        );
//
//        Job updatedJob = new Job();
//        updatedJob.setId(1L);
//        updatedJob.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
//
//        String json = this.objectMapper.writeValueAsString(jobDto);
//
//        // Given
//        given(this.jobService.update(eq(1L), Mockito.any(Job.class))).willReturn(updatedJob);
//
//        // When and then
//        this.mockMvc.perform(put(this.baseUrl + "/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag").value(true))
//                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Update Success"))
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.description").value("Testare på Tesla."))
//                .andExpect(jsonPath("$.data.user").isEmpty());
//    }

//    @Test
//    void testUpdateJobWithNonExistentId() throws Exception {
//        Long id = Mockito.any(Long.class);
//
//        // Given
//        JobDtoForm jobDto = new JobDtoForm(
//                "m@e.se",
//                "Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."
//        );
//
//        String json = this.objectMapper.writeValueAsString(jobDto);
//
//
//        given(this.jobService.update(id, Mockito.any(Job.class))).willThrow(new ObjectNotFoundException("job", id));
//
//        // When and then
//        this.mockMvc.perform(put(this.baseUrl + "/update/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.flag").value(false))
//                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
//                .andExpect(jsonPath("$.message").value("Could not find job with Id " + id))
//                .andExpect(jsonPath("$.data").isEmpty());
//    }

    @Test
    void testDeleteSuccess() throws Exception {
        // Given
        doNothing().when(this.jobService).delete(1L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNonExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("job", 0L)).when(this.jobService).delete(0L);

        this.mockMvc.perform(delete(this.baseUrl + "/delete/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id 0"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddJobSuccess() throws Exception {
        User user = new User();
        user.setUsername("Mikael");
        user.setRoles("admin user");
        user.setEmail("m@e.se");

        JobDtoForm newJobDtoForm = new JobDtoForm(
                "m@e.se",
                "Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");

        Job savedJob = new Job();
        savedJob.setId(1L);
        savedJob.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        savedJob.setUser(user);

        String json = this.objectMapper.writeValueAsString(newJobDtoForm);

        // Given
        given(this.jobService.addJob(newJobDtoForm)).willReturn(savedJob);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/addJob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.description").value("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."));
    }
}