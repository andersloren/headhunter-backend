package se.sprinta.headhunterbackend.job;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Job is an entity that stores information on a job that a user wants to create ads for.
 * Relationship: [Ad] *...1 [Job] *...1 [User]
 */

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The job has a title that the user uses to identify its jobs.
     */

    // TODO: 14/03/2024 Remove @NotEmpty? It's commented out.
//    @NotEmpty(message = "title is required")
    private String title;

    /**
     * Contact information
     */

    private String recruiterName;

    private String adCompany;

    private String adEmail;

    private String adPhone;

    private String applicationDeadline;

    /**
     * The job has a description that the AI will use to generate an ad.
     */

    @Column(columnDefinition = "TEXT")
    // TODO: 14/03/2024 Remove @NotEmpty? It's commented out.
//    @NotEmpty(message = "description is required.")
    private String description;

    /**
     * The job has an instruction that the AI will use to handle the description of the Job.
     */

    @Column(columnDefinition = "TEXT")
    private String instruction;

    /**
     * The job belongs to a user.
     * Relationship: [Job] *...1 [User]
     */

    @ManyToOne()
    @JoinColumn(name = "\"user_id\"")
    private User user;

    /**
     * Every Job object belongs to a User object.
     * Relationship: [Job] *...1 [User]
     */

    @OneToMany(mappedBy = "job", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    // TODO: 02/03/2024 add cascade when needed
    private List<Ad> ads = new ArrayList<>();

    /**
     * The number of ads that a Job object holds.
     */

    private int numberOfAds;

    /**
     * When an Ad object is created, it has to be connected to a Job object.
     *
     * @param ad Ad is the object that holds the AI-generated data.
     */

    public void addAd(Ad ad) {
        this.ads.add(ad);
        setNumberOfAds();
    }

    /**
     * When an Ad object is deleted, it has to be unconnected to the Job object it used to belong to.
     *
     * @param ad Ad is the object that holds the AI-generated data.
     */

    // TODO: 14/03/2024 Remove this method? Cascade solves this?
    public void removeAd(Ad ad) {
        this.ads.remove(ad);
        setNumberOfAds();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}