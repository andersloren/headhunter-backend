package se.sprinta.headhunterbackend.job;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.ad.Ad;
import se.sprinta.headhunterbackend.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "jobs")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @NotEmpty(message = "title is required")
    private String title;

    @Column(columnDefinition = "TEXT")
//    @NotEmpty(message = "description is required.")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String instruction;

    @Column(columnDefinition = "TEXT") // TODO: 02/03/2024 remove this once the ad setup is all finished 
    private String htmlCode;

    @OneToMany(mappedBy = "job") // TODO: 02/03/2024 add cascade when needed
    private List<Ad> ads = new ArrayList<>();

    private int numberOfAds;

    public void addAd(Ad ad) {
        this.ads.add(ad);
        setNumberOfAds();
    }

    public void removeAd(Ad ad) {
        this.ads.remove(ad);
        setNumberOfAds();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public int getNumberOfAds() {
        return numberOfAds;
    }

    public void setNumberOfAds() {
        this.numberOfAds = this.ads.size();
    }
}