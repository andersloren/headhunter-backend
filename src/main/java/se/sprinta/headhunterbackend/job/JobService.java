package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Message;
import se.sprinta.headhunterbackend.job.dto.JobDtoForm;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserRepository;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    private final ChatClient chatClient;

    public JobService(JobRepository jobRepository, UserRepository userRepository, ChatClient chatClient) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.chatClient = chatClient;
    }

    public Job save(Job job) {
        return this.jobRepository.save(job);
    }

    public List<Job> findAll() {
        return this.jobRepository.findAll();
    }

    public Job findById(Long id) {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
    }

    public Job update(Long id, Job update) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
        job.setDescription(update.getDescription());
        // // TODO: 06/02/2024 add more statements here if Job gets additional fields
        return this.jobRepository.save(job);
    }

    public void delete(Long id) {
        Job foundJob = this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
        this.jobRepository.delete(foundJob);
    }

    public Job addJob(JobDtoForm jobDtoForm) {
        System.out.println("addJobb init");
        Job newJob = new Job();
        newJob.setDescription(jobDtoForm.description());
        System.out.println("newJob :" + newJob);

        Job savedJob = this.jobRepository.save(newJob);
        System.out.println("newJob :" + savedJob);

        User foundUser = this.userRepository.findByEmail(jobDtoForm.email())
                .orElseThrow(() -> new ObjectNotFoundException("user", jobDtoForm.email()));
        foundUser.addJob(newJob);
        System.out.println("foundUser :" + foundUser);

        return savedJob;
    }

    public String generate(Long id) throws JsonProcessingException {

        Job foundJob = this.jobRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("job", id));

        // Prepare the message for summarizing
        List<Message> messages = List.of(
                new Message("system", "Kan du göra en professionell jobbannons i HTML av detta utkast."),
                new Message("user", foundJob.getDescription()));

        ChatRequest chatRequest = new ChatRequest("gpt-4", messages);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest); // Tell chatClient to generate a job ad based on the given chatRequest
        return chatResponse.choices().get(0).message().content();
    }
}
