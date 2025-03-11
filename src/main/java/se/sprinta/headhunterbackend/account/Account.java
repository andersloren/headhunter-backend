package se.sprinta.headhunterbackend.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Getter
    @Id
    @NotEmpty(message = "email is required.") // Is checked on AccountDtoForm when registering a new Account
    private String email;

    @Getter
    @NotEmpty(message = "password is required.") // Is checked on AccountDtoForm when registering a new Account
    private String password;

    /**
     * An Account object has specific roles that gives its user authorities to certain parts of the app.
     */

    @Getter
    private String roles;

    /**
     * An Account object has an integer number of jobs.
     */

    @Getter
    private long number_of_jobs;

    /**
     * When adding this to the code, make sure to adjust init.sql too
     */

    @OneToOne(mappedBy = "account")
    private AccountInfo accountInfo;

    /**
     * An Account object has an array of Job objects.
     * Relationship: [Account] 1...* [Job]
     */

    private boolean isVerified = false;

    @Getter
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
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

    public void setNumber_of_jobs() {
        this.number_of_jobs = this.jobs.size();
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return number_of_jobs == account.number_of_jobs && isVerified == account.isVerified && Objects.equals(email, account.email) && Objects.equals(roles, account.roles) && Objects.equals(accountInfo, account.accountInfo) && Objects.equals(jobs, account.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, roles, number_of_jobs, accountInfo, isVerified, jobs);
    }

// TODO: 04/07/2024 Remove when going into production?


    @Override
    public String toString() {
        return "Account{" +
                "email='" + email + '\'' +
                ", roles='" + roles + '\'' +
                ", number_of_jobs=" + number_of_jobs +
                ", accountInfo=" + accountInfo +
                ", isVerified=" + isVerified +
//                ", jobs=" + jobs +
                '}';
    }
}
