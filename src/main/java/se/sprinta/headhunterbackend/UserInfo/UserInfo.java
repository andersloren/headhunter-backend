package se.sprinta.headhunterbackend.UserInfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.user.User;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "\"user_info\"")
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String organization;

    @OneToOne()
    @JoinColumn(name = "\"user\"")
    private User user;

    public UserInfo(String name, String organization, User user) {
        this.name = name;
        this.organization = organization;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


