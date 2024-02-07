package se.sprinta.headhunterbackend.job;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.sprinta.headhunterbackend.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024) // TODO: 06/02/2024 Should this be increased, or should we keep it?
    @NotEmpty(message = "description is required.")
    private String description;


    /*
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "user_id", nullable = false)
     * private User user;
     * @Column(nullable = false)
     * private String title;
     * @Column(nullable = false)
     * private String qualifications;
     * @Column(nullable = false)
     * private String desirable;
     * @Column(nullable = false)
     * private String mandatory;
     * @Temporal(TemporalType.TIMESTAMP)
     * @Column(name = "created_at", nullable = false)
     * private Date createdAt;
     * @Column(name = "status", nullable = false)
     * private String status;
     * @Temporal(TemporalType.DATE)
     * @Column(name = "expiration_date")
     * private Date expirationDate;
     **/

}