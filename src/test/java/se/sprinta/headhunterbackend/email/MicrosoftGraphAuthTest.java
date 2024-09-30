package se.sprinta.headhunterbackend.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("auth-test")
class MicrosoftGraphAuthTest {

    @Autowired
    private MicrosoftGraphAuth microsoftGraphAuth;

//    @Test
//    @DisplayName("Test getUserInfo")
//    void test_GetUserInfo() throws IOException, URISyntaxException {
//        this.microsoftGraphAuth.getUserInfo();
//    }

    @Test
    @DisplayName("Test Send Email")
    void test_SendEmail() throws IOException, URISyntaxException {
        this.microsoftGraphAuth.sendEmail();
    }
}