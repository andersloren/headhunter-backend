package se.sprinta.headhunterbackend.ad;

import jakarta.persistence.*;
import lombok.*;
import se.sprinta.headhunterbackend.job.Job;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ads")
public class Ad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String htmlCode;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

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
}
