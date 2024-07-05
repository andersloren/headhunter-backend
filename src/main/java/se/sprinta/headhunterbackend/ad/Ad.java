package se.sprinta.headhunterbackend.ad;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Ad is an entity that stores AI-generated data based on field values from a job.
 * Relationship: [Ad] *...1 [Job]
 */

@AllArgsConstructor
@Entity
@Table(name = "ad")
public class Ad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * htmlCode holds a String that makes up a job ad in Html-format
     */

    @Column(columnDefinition = "TEXT")
    private String htmlCode;

    /**
     * dateCreated is a timestamp for when the entity was originally created.
     * Used for sorting ads chronologically.
     */

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;

    /**
     * Every Ad is part of a Job
     * Relationship: [Ad] *...1 [Job]
     */

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Ad() {
        this.dateCreated = LocalDate.now();
    }

    public Ad(String htmlCode) {
        this();
        this.htmlCode = htmlCode;
    }

    public Ad(String id, String htmlCode) {
        this();
        this.id = id;
        this.htmlCode = htmlCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return Objects.equals(id, ad.id) && Objects.equals(htmlCode, ad.htmlCode) && Objects.equals(dateCreated, ad.dateCreated) && Objects.equals(job, ad.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, htmlCode, dateCreated, job);
    }

    // TODO: 04/07/2024 Remove when going into production?

    @Override
    public String toString() {
        return "Ad{" +
                "id='" + id + '\'' +
                ", htmlCode='" + htmlCode + '\'' +
                ", dateCreated=" + dateCreated +
                ", job=" + job +
                '}';
    }
}
