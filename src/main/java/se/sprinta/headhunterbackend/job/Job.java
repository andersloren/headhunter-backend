package se.sprinta.headhunterbackend.job;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.account.Account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Job is an entity that stores information on a job that an account wants to create ads for.
 * Relationship: [Ad] *...1 [Job] *...1 [Account]
 */

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The job has a title that the account uses to identify its jobs.
     */

    // TODO: 14/03/2024 Remove @NotEmpty? It's commented out.
//    @NotEmpty(message = "title is required")
    private String title;

    /**
     * The job has a description that the AI will use to generate an ad.
     */

    @Column(columnDefinition = "TEXT")
    // TODO: 14/03/2024 Remove @NotEmpty? It's commented out.
//    @NotEmpty(message = "description is required")
    private String description;

    /**
     * The job has an instruction that the AI will use to handle the description of the Job.
     */

    @Column(columnDefinition = "TEXT")
    private String instruction;

    /**
     * Contact information
     */

    private String recruiterName;

    private String adCompany;

    private String adEmail;

    private String adPhone;

    private String applicationDeadline;

    /**
     * The job belongs to an account.
     * Relationship: [Job] *...1 [Account]
     */

    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    /**
     * Every Job object belongs to an account object.
     * Relationship: [Job] *...1 [Account]
     */

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    // TODO: 02/03/2024 add cascade when needed
    private List<Ad> ads = new ArrayList<>();

    /**
     * The number of ads that a Job object holds.
     */

    private int numberOfAds;

    public Job(String title, String description, String instruction) {
        this.title = title;
        this.description = description;
        this.instruction = instruction;
    }

    /**
     * When an Ad object is created, it has to be connected to a Job object.
     *
     * @param newAd Ad is the object that holds the AI-generated data.
     */


    public void addAd(Ad newAd) {
        if (newAd == null) throw new NullPointerException("Can't add null Job");
        if (newAd.getJob() != null) throw new IllegalStateException("Ad already has a job");
        this.getAds().add(newAd);
        this.setNumberOfAds();
        newAd.setJob(this);
    }

    /**
     * When an Ad is deleted, it has to be disassociated with its Job object.
     *
     * @param oldAd is the object to be removed that holds AI-generated data.
     */

    public void removeAd(Ad oldAd) {
        if (oldAd == null) throw new NullPointerException("Can't remove null Job");
        if (oldAd.getJob() == null) throw new IllegalStateException("This ad doesn't belong to this Job");
        this.getAds().remove(oldAd);
        this.setNumberOfAds();
        oldAd.setJob(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public int getNumberOfAds() {
        return numberOfAds;
    }

    /**
     * The number of ads a Job object holds is dynamically calculated
     * during the conversion from a Job object to a JobDtoView object
     */

    public void setNumberOfAds() {
        this.numberOfAds = this.ads.size();
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getAdCompany() {
        return adCompany;
    }

    public void setAdCompany(String adCompany) {
        this.adCompany = adCompany;
    }

    public String getAdEmail() {
        return adEmail;
    }

    public void setAdEmail(String adEmail) {
        this.adEmail = adEmail;
    }

    public String getAdPhone() {
        return adPhone;
    }

    public void setAdPhone(String adPhone) {
        this.adPhone = adPhone;
    }

    public String getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    // TODO: 04/07/2024 Remove when going into production?

    @Override
    public String toString() {
        return "Job{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instruction='" + instruction + '\'' +
                ", recruiterName='" + recruiterName + '\'' +
                ", adCompany='" + adCompany + '\'' +
                ", adEmail='" + adEmail + '\'' +
                ", adPhone='" + adPhone + '\'' +
                ", applicationDeadline='" + applicationDeadline + '\'' +
                '}';
    }
}