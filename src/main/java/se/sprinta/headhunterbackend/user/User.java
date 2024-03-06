package se.sprinta.headhunterbackend.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private String roles;

    private int numberOfJobs;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Job> jobs = new ArrayList<>();

    public User(String email, String username, String roles, List<Job> jobs) {
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.numberOfJobs = getNumberOfJobs();
        this.jobs = jobs;
    }

    public void addJob(Job newJob) {
        this.jobs.add(newJob);
        setNumberOfJobs();
    }

    public void removeJob(Job newJob) {
        this.jobs.remove(newJob);
        setNumberOfJobs();
    }

    // TODO: 08/02/2024 create removeJob

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

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
