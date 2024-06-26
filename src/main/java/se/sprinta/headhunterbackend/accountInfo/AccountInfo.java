package se.sprinta.headhunterbackend.accountInfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import se.sprinta.headhunterbackend.account.Account;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "account_info")
public class AccountInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String organization;

    @OneToOne()
    @JoinColumn(name = "account")
    private Account account;

    public AccountInfo(String name, String organization, Account account) {
        this.name = name;
        this.organization = organization;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}


