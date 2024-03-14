package se.sprinta.headhunterbackend.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User is an entity that stores information that regards the user's account and the Job objects it holds.
 * Relationship: [User] 1...* [Job]
 */

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "_users")
public class User implements Serializable {

    @Id
    @NotEmpty(message = "email is required.")
    private String email;

    @NotEmpty(message = "username is required.")
    private String username;

    @NotEmpty(message = "password is required.")
    private String password;

    /**
     * A User object has specific roles that gives its user authorities to certain parts of the app.
     */

    private String roles;

    /**
     * A User object has an integer number of jobs.
     */

    private int numberOfJobs;

    /**
     * A User object has an array of Job objects.
     * Relationship: [User] 1...* [Job]
     */

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Job> jobs = new ArrayList<>();

    public User(String username, String roles) {
        this.username = username;
        this.roles = roles;
    }

    public User(String email, String username, String roles, List<Job> jobs) {
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.numberOfJobs = getNumberOfJobs();
        this.jobs = jobs;
    }

    /**
     * When a Job object is created, it has to be connected to a User object.
     *
     * @param newJob This is the Job object that the User will take ownership of.
     */

    public void addJob(Job newJob) {
        this.jobs.add(newJob);
        setNumberOfJobs();
    }

    /**
     * When a Job object is deleted, it has to be disassociated with the User object it used to belong to.
     *
     * @param newJob This is the Job object that the User will take ownership of.
     */

    public void removeJob(Job newJob) {
        this.jobs.remove(newJob);
        setNumberOfJobs();
    }

    // TODO: 08/02/2024 create removeJob

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    /**
     * The number of jobs a User object holds is dynamically calculated
     * during the conversion from a User object to a UserDtoView object
     */

    public void setNumberOfJobs() {
        numberOfJobs = this.jobs.size();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
