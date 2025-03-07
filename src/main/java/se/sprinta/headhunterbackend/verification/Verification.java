package se.sprinta.headhunterbackend.verification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import se.sprinta.headhunterbackend.account.Account;

import java.util.Random;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String verificationCode;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Verification(Account account) {
        this.account = account;
    }

    public Verification() {
        this.verificationCode = generateVerificationCode();
    }

    public String generateVerificationCode() {
        Random random = new Random();

        return String.valueOf(random.nextInt(10000, 99999));
    }
}
