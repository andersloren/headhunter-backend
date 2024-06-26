package se.sprinta.headhunterbackend.ad;

import jakarta.persistence.*;
import lombok.*;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
     * createdDateTime is a timestamp for when the entity was originally created.
     * Used for sorting ads chronologically.
     */

    private ZonedDateTime createdDateTime = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));

    /**
     * Every Ad is part of a Job
     * Relationship: [Ad] *...1 [Job]
     */

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Ad() {
    }

    public Ad(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public Ad(String id, String htmlCode) {
        this.id = id;
        this.htmlCode = htmlCode;
    }

    public Ad(String htmlCode, Job job) {
        this.htmlCode = htmlCode;
        this.job = job;
    }

    public Ad(String id, String htmlCode, Job job) {
        this.id = id;
        this.htmlCode = htmlCode;
        this.job = job;
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

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Timezone must be taken into consideration.
     */

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
