package se.sprinta.headhunterbackend.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "_user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "jobs"})
public class User implements Serializable {

    @Id
    @NotEmpty(message = "email is required.")
    private String email;

    @NotEmpty(message = "username is required.")
    private String username;

    @NotEmpty(message = "password is required.")
    private String password;

    private String roles;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<Job> jobs = new ArrayList<>();

    public void addJob(Job job) {
        job.setUser(this);
        this.jobs.add(job);
    }

    public void removeJob(Job job) {
        job.setUser(null);
        this.jobs.remove(job);
    }
}
