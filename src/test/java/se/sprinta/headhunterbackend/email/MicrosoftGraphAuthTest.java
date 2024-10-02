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
    @DisplayName("Test Send Email")
    void test_SendEmail() throws IOException, URISyntaxException {

        String email = "tsetse.betel4465@eagereverest.com";

        this.microsoftGraphAuth.sendEmail(email);
    }
}