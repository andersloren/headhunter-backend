package se.sprinta.headhunterbackend.job;
import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private String title;

    @Column(nullable = false, length = 1024)
    private String description;

//    @Column(nullable = false)
//    private String qualifications;
//
//    @Column(nullable = false)
//    private String desirable;
//
//    @Column(nullable = false)
//    private String mandatory;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_at", nullable = false)
//    private Date createdAt;
//
//    @Column(name = "status", nullable = false)
//    private String status;
//
//    @Temporal(TemporalType.DATE)
//    @Column(name = "expiration_date")
//    private Date expirationDate;

    // Constructors
    public Job() {}

    public Job(Long id, String description
//                            String title, User user, Date createdAt, String status, Date expirationDate
                            ) {
        this.id = id;
//        this.title = title;
        this.description = description;
//        this.user = user;
//        this.createdAt = createdAt;
//        this.status = status;
//        this.expirationDate = expirationDate;
    }

    // Getters
    public Long getId() { return id; }

//    public String getTitle() { return title; }

    public String getDescription() { return description; }

//    public User getUser() { return user; }
//
//    public Date getCreatedAt() { return createdAt; }
//
//    public String getStatus() { return status; }
//
//    public Date getExpirationDate() { return expirationDate; }

    // Setters
    public void setId(Long id) { this.id = id; }

//    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

//    public void setUser(User user) { this.user = user; }
//
//    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
//
//    public void setStatus(String status) { this.status = status; }
//
//    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
}