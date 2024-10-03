package se.sprinta.headhunterbackend.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
@ActiveProfiles("auth-test")
class MicrosoftGraphAuthTest {

    @Autowired
    private MicrosoftGraphAuth microsoftGraphAuth;

    @Test
    @DisplayName("Test Send Verification Email")
    void test_SendVerificationEmail() throws IOException, URISyntaxException {

        String email = "tsetse.betel4465@eagereverest.com";
        String verificationCode = "12345";

        this.microsoftGraphAuth.sendVerificationEmail(email, verificationCode);
    }

    @Test
    @DisplayName("Test Send Confirmation Email")
    void test_SendConfirmationEmail() throws IOException, URISyntaxException {

        String email = "tsetse.betel4465@eagereverest.com";
        String verificationCode = "12345";

        this.microsoftGraphAuth.sendConfirmationEmail(email);
    }
}