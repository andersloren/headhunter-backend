package se.sprinta.headhunterbackend.job;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @Column(length = 5_000) // TODO: 08/02/2024 What should this number be (looking into the future)?
    @NotEmpty(message = "description is required.")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    private String instruction;

    @Column(length = 10_000) // TODO: 08/02/2024 What should this number be (looking into the future)?
    private String htmlCode;
}