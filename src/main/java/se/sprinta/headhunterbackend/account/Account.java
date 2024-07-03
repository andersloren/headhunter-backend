package se.sprinta.headhunterbackend.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.query.named.FetchMemento;
import se.sprinta.headhunterbackend.accountInfo.AccountInfo;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Account is an entity that stores information that regards the user's account and the Job objects it holds.
 * Relationship: [Account] 1...* [Job]
 */

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "account")
public class Account implements Serializable {

    @Id
    @NotEmpty(message = "email is required.") // Is checked on AccountDtoForm when registering a new Account
    private String email;

    @NotEmpty(message = "password is required.") // Is checked on AccountDtoForm when registering a new Account
    private String password;

    /**
     * An Account object has specific roles that gives its user authorities to certain parts of the app.
     */

    private String roles;

    /**
     * An Account object has an integer number of jobs.
     */

    private long number_of_jobs;

    @OneToOne(mappedBy = "account")
    private AccountInfo accountInfo;

    /**
     * An Account object has an array of Job objects.
     * Relationship: [Account] 1...* [Job]
     */

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER
            , cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<Job> jobs = new ArrayList<>();

    public Account(String roles) {
        this.roles = roles;
    }

    public Account(String email, String roles) {
        this.email = email;
        this.roles = roles;
    }

    public void addJob(Job newJob) {
        if (newJob == null) throw new NullPointerException("Can't add null Job");
        if (newJob.getAccount() != null) throw new NullPointerException("Job already has a user");
        this.getJobs().add(newJob);
        this.setNumber_of_jobs();
        newJob.setAccount(this);
    }

    public void removeJob(Job oldJob) {
        if (oldJob == null) throw new NullPointerException("Can't remove null Job");
        this.getJobs().remove(oldJob);
        this.setNumber_of_jobs();
        oldJob.setAccount(null);
    }

    public long getNumber_of_jobs() {
        return this.number_of_jobs;
    }

    public void setNumber_of_jobs() {
        this.number_of_jobs = this.jobs.size();
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (this != obj || getClass() != obj.getClass()) return false;
//
//        Account account = (Account) obj;
//        return email.equals(account.email) && (Objects.equals(email, account.email));
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        // Password is left out !!!
        return number_of_jobs == account.number_of_jobs &&
                Objects.equals(email, account.email) &&
                Objects.equals(roles, account.roles) &&
                Objects.equals(accountInfo, account.accountInfo) &&
                Objects.equals(jobs, account.jobs);
    }

    @Override
    public int hashCode() {
        // Password is left out !!!
        return Objects.hash(
                email,
                roles,
                number_of_jobs,
                accountInfo,
                jobs);
    }
}
