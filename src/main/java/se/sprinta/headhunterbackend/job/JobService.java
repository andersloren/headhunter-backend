package se.sprinta.headhunterbackend.job;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.AccountRepository;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.ad.AdRepository;
import se.sprinta.headhunterbackend.client.chat.ChatClient;
import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;
import se.sprinta.headhunterbackend.client.chat.dto.Message;
import se.sprinta.headhunterbackend.job.dto.JobCardDtoView;
import se.sprinta.headhunterbackend.job.dto.JobDtoFormUpdate;
import se.sprinta.headhunterbackend.job.dto.JobDtoView;
import se.sprinta.headhunterbackend.system.exception.DoesNotExistException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.utils.HtmlUtilities;

import java.util.List;

/**
 * Business logic for Job
 */

@Service
@Transactional
public class JobService {
  private final JobRepository jobRepository;
  private final AdRepository adRepository;
  private final AccountRepository accountRepository;
  private final ChatClient chatClient;
  private final HtmlUtilities htmlUtilities;

  public JobService(JobRepository jobRepository,
      AdRepository adRepository,
      AccountRepository accountRepository,
      ChatClient chatClient,
      HtmlUtilities htmlUtilities) {
    this.jobRepository = jobRepository;
    this.adRepository = adRepository;
    this.accountRepository = accountRepository;
    this.chatClient = chatClient;
    this.htmlUtilities = htmlUtilities;
  }

  public List<Job> findAll() {
    return this.jobRepository.findAll();
  }

  public List<JobDtoView> getJobDtos() {
    return this.jobRepository.getJobDtos();
  }

  public List<JobDtoView> getJobDtosByEmail(String email) {
    this.accountRepository.findById(email)
        .orElseThrow(() -> new ObjectNotFoundException("account", email));

    return this.jobRepository.getJobDtosByEmail(email);
  }

  public List<JobCardDtoView> getJobCardDtosByEmail(String email) {
    this.accountRepository.findById(email)
        .orElseThrow(() -> new ObjectNotFoundException("account", email));

    return this.jobRepository.getJobCardDtosByEmail(email);
  }

  public Job findById(long jobId) {
    return this.jobRepository.findById(jobId)
        .orElseThrow(() -> new ObjectNotFoundException("job", jobId));
  }

  public JobDtoView getJobDto(Long id) {
    return this.jobRepository.getJobDto(id).orElseThrow(() -> new ObjectNotFoundException("job", id));
  }

  public Job addJob(String email, Job newJob) {

    Account foundAccount = this.accountRepository.findAccountByEmail(email)
        .orElseThrow(() -> new ObjectNotFoundException("account", email));

    foundAccount.addJob(newJob);

    // Dirty check on foundUser, so is automatically persisted
    return this.jobRepository.save(newJob);
  }

  public Job update(Long id, JobDtoFormUpdate update) {
    if (update == null)
      throw new NullPointerException("Update can't be null");

    Job job = this.jobRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("job", id));

    job.setTitle(update.title());
    job.setDescription(update.description());
    job.setInstruction(update.instruction());
    job.setRecruiterName(update.recruiterName());
    job.setAdCompany(update.adCompany());
    job.setAdEmail(update.adEmail());
    job.setAdPhone(update.adPhone());
    job.setApplicationDeadline(update.applicationDeadline());

    return this.jobRepository.save(job);
  }

  public void delete(String email, Long jobId) {

    Account foundAccount = this.accountRepository.findById(email)
        .orElseThrow(() -> new ObjectNotFoundException("account", email));

    Job foundJob = this.jobRepository.findById(jobId)
        .orElseThrow(() -> new ObjectNotFoundException("job", jobId));

    foundAccount.removeJob(foundJob);

    this.jobRepository.delete(foundJob);
  }

  public String generate(Long id) {

    Job foundJob = this.jobRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("job", id));

    // Prepare the message for summarizing
    List<Message> messages = List.of(
        new Message("system", foundJob.getInstruction()),
        new Message("user", foundJob.getDescription()));

    ChatRequest chatRequest = new ChatRequest("gpt-4o", messages);

    ChatResponse chatResponse = this.chatClient.generate(chatRequest); // Tell chatClient to generate a job ad based on
    // the given chatRequest

    String response = chatResponse.choices().get(0).message().content();

    // To trim the response, response is being passed to makeResponseSubstring and a
    // trimmed string is returned

    String substringResponse = this.htmlUtilities.makeHtmlResponseSubstring(response);

    Ad newHtmlAd = new Ad(substringResponse);
    foundJob.addAd(newHtmlAd);
    newHtmlAd.setJob(foundJob);

    this.adRepository.save(newHtmlAd);
    return substringResponse;
  }
}
