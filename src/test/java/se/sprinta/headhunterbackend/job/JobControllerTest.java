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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.job.converter.JobToJobDtoViewConverter;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormRemove;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.DoesNotExistException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
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

    @MockBean
    UserService userService;

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
        user.setUsername("Mikael");
        user.setPassword("a");
        user.setRoles("admin user");

        Job j1 = new Job();
        j1.setId(1L);
        j1.setTitle("Java-utvecklare");
        j1.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        j1.setUser(user);
        j1.setInstruction("This is an instruction");

        Job j2 = new Job();
        j2.setId(2L);
        j1.setTitle(".Net-junior");
        j2.setDescription(".Net-junior till vårt nya kontor.");
        j2.setUser(user);
        j2.setInstruction("This is an instruction");

        Job j3 = new Job();
        j3.setId(3L);
        j1.setTitle("HR-ninja");
        j3.setDescription("HR-ninja till vår nya avdelning på Mynttorget.");
        j3.setUser(user);
        j3.setInstruction("This is an instruction");

        user.addJob(j1);
        user.addJob(j2);
        user.addJob(j3);

        this.jobs = new ArrayList<>();

        this.jobs.add(j1);
        this.jobs.add(j2);
        this.jobs.add(j3);
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
                .andExpect(jsonPath("$.data[0].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].description").value(".Net-junior till vårt nya kontor."))
                .andExpect(jsonPath("$.data[1].email").value("m@e.se"))
                .andExpect(jsonPath("$.data[2].id").value(3L))
                .andExpect(jsonPath("$.data[2].description").value("HR-ninja till vår nya avdelning på Mynttorget."))
                .andExpect(jsonPath("$.data[2].email").value("m@e.se"));
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
                .andExpect(jsonPath("$.data.description").value("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."))
                .andExpect(jsonPath("$.data.email").value("m@e.se"));
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

    @Test
    void testUpdateJobSuccess() throws Exception {

        Job job = new Job();
        job.setId(1L);
        job.setTitle("Old Title");
        job.setDescription("Old Description");
        job.setInstruction("Old Instruction");

        User user = new User();
        user.setEmail("m@e.se");
        user.setUsername("Mikael");
        user.setRoles("admin user");
        user.addJob(job);

        JobDtoFormUpdate jobDtoFormUpdate = new JobDtoFormUpdate(
                "m@e.se",
                "New Title",
                "New Description",
                "New Instruction"
        );

        String json = this.objectMapper.writeValueAsString(jobDtoFormUpdate);

        Job update = new Job();
        update.setId(1L);
        update.setUser(user);
        update.setDescription("New Description");
        update.setInstruction("New Instruction");

        // Given
        given(this.jobService.update(1L, jobDtoFormUpdate)).willReturn(update);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.description").value("New Description"))
                .andExpect(jsonPath("$.data.instruction").value("New Instruction"));

    }

    @Test
    void testUpdateJobWithNonExistentId() throws Exception {
        Long id = Mockito.any(Long.class);

        // Given
        JobDtoFormUpdate jobDto = new JobDtoFormUpdate(
                "m@e.se",
                "New Title",
                "New Description",
                "New Instruction"
        );

        String json = this.objectMapper.writeValueAsString(jobDto);

        given(this.jobService.update(id, eq(jobDto))).willThrow(new ObjectNotFoundException("job", id));

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id " + id))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteSuccess() throws Exception {
        JobDtoFormRemove removeJob = new JobDtoFormRemove("m@e.se", 1L);

        String json = this.objectMapper.writeValueAsString(removeJob);

        // Given
        doNothing().when(this.jobService).delete("m@e.se", 1L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/m@e.se" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNonExistentId() throws Exception {
        JobDtoFormRemove removeJob = new JobDtoFormRemove("m@e.se", 0L);

        String json = this.objectMapper.writeValueAsString(removeJob);

        // Given
        doThrow(new ObjectNotFoundException("job", 0L)).when(this.jobService).delete("m@e.se", 0L);

        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/m@e.se" + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find job with Id 0"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNonExistentEmail() throws Exception {
        JobDtoFormRemove removeJob = new JobDtoFormRemove("m@j.se", 1L);

        String json = this.objectMapper.writeValueAsString(removeJob);

        // Given
        doThrow(new ObjectNotFoundException("user", "m@j.se")).when(this.jobService).delete("m@j.se", 1L);

        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/m@j.se" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Email m@j.se"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWithWrongEmail() throws Exception {
        // Setup
        User user = new User();
        user.setEmail("m@e.se");
        this.userService.save(user);

        JobDtoFormAdd newJob = new JobDtoFormAdd("m@e.se", "This is a title", "This is a description.", "instruction");
        this.jobService.addJob(newJob);

        JobDtoFormRemove removeJob = new JobDtoFormRemove("a@l.se", 1L);
        String json = this.objectMapper.writeValueAsString(removeJob);

        // Given
        doThrow(new DoesNotExistException()).when(this.jobService).delete("a@l.se", 1L);

        this.mockMvc.perform(delete(this.baseUrl + "/delete" + "/a@l.se" + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Does not exist"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddJobSuccess() throws Exception {
        User user = new User();
        user.setUsername("Mikael");
        user.setRoles("admin user");
        user.setEmail("m@e.se");

        JobDtoFormAdd newJobDtoFormAdd = new JobDtoFormAdd(
                "m@e.se",
                "Java-utvecklare",
                "Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.", "instruction");

        Job savedJob = new Job();
        savedJob.setId(1L);
        savedJob.setDescription("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten.");
        savedJob.setUser(user);

        String json = this.objectMapper.writeValueAsString(newJobDtoFormAdd);

        // Given
        given(this.jobService.addJob(newJobDtoFormAdd)).willReturn(savedJob);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/addJob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.description").value("Erfaren Java-utvecklare till vårt nya uppdrag hos Försvarsmakten."))
                .andExpect(jsonPath("$.data.email").value("m@e.se"));
    }
}