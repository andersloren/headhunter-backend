package se.sprinta.headhunterbackend.ad;

import jakarta.persistence.*;
import lombok.*;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@AllArgsConstructor

@Entity
@Table(name = "ads")
public class Ad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String htmlCode;

    private ZonedDateTime createdDateTime = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));

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
