package se.sprinta.headhunterbackend.job;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Message;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormAdd;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormRemove;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.system.exception.DoesNotExistException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.system.exception.ResponseSubstringNotPureHtmlException;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public List<Job> findAllJobsByEmail(String email) {
        List<Job> allJobs = this.jobRepository.findAll();
        return allJobs.stream().filter(job -> job.getUser().getEmail().equalsIgnoreCase(email)).collect(Collectors.toList());
    }

    public Job findById(Long id) {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));
    }


    public Job addJob(JobDtoFormAdd jobDtoFormAdd) {
        User foundUser = this.userRepository.findByEmail(jobDtoFormAdd.email())
                .orElseThrow(() -> new ObjectNotFoundException("user", jobDtoFormAdd.email()));

        Job newJob = new Job();
        newJob.setTitle(jobDtoFormAdd.title());
        newJob.setDescription(jobDtoFormAdd.description());
        newJob.setInstruction(jobDtoFormAdd.instruction());
        newJob.setUser(foundUser);

        foundUser.addJob(newJob);
        foundUser.setNumberOfJobs();

        this.userRepository.save(foundUser);

        return this.jobRepository.save(newJob);
    }

    public Job update(Long id, JobDtoFormUpdate update) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("job", id));

        if (update.title() != null) {
            job.setTitle(update.title());
        }
        if (update.description() != null) {
            job.setDescription(update.description());
        }
        if (update.instruction() != null) {
            job.setInstruction(update.instruction());
        }
        if (update.htmlCode() != null) {
            job.setHtmlCode(update.htmlCode());
        }

        // // TODO: 06/02/2024 add more statements here if Job gets additional fields
        return this.jobRepository.save(job);
    }

    public void delete(JobDtoFormRemove jobDtoFormRemove) {

        Job foundJob = this.jobRepository.findById(jobDtoFormRemove.id())
                .orElseThrow(() -> new ObjectNotFoundException("job", jobDtoFormRemove.id()));

        User foundUser = this.userRepository.findByEmail(jobDtoFormRemove.email())
                .orElseThrow(() -> new ObjectNotFoundException("user", jobDtoFormRemove.email()));


        if (!foundJob.getUser().getEmail().equalsIgnoreCase(foundUser.getEmail())) {
            throw new DoesNotExistException();
        }

        foundUser.removeJob(foundJob);

        this.jobRepository.delete(foundJob);
    }

    public String generate(Long id) {

        Job foundJob = this.jobRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("job", id));
        String instruction1 = foundJob.getInstruction();

        // Prepare the message for summarizing
        List<Message> messages = List.of(
                new Message("system", instruction1),
                new Message("user", foundJob.getDescription()));

        ChatRequest chatRequest = new ChatRequest("gpt-4", messages);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest); // Tell chatClient to generate a job ad based on the given chatRequest

        String response = chatResponse.choices().get(0).message().content();

        // To trim the response, response is being passed to makeResponseSubstring and a trimmed string is returned
        String substringResponse = makeResponseSubstring(response);
        foundJob.setHtmlCode(substringResponse); // TODO: 02/03/2024  remove this once the ad setup is all finished 

        Ad newAd = new Ad(substringResponse);
        foundJob.addAd(newAd);
        newAd.setJob(foundJob);

        this.jobRepository.save(foundJob);
        return substringResponse;
    }

    public String makeResponseSubstring(String response) {
        if (response == null) throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);


        int cutBeginning = response.indexOf("<!D");
        // Adjusting cutEnd to include the entire "</html>" tag
        int cutEnd = response.lastIndexOf("</html>") + "</html>".length();

        // Extracting the substring
        if (cutBeginning == -1 || cutEnd == -1 || cutEnd <= cutBeginning)
            throw new ResponseSubstringNotPureHtmlException("HTML");

        return response.substring(cutBeginning, cutEnd);
    }
}

