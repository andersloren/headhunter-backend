package se.sprinta.headhunterbackend.system;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.DisplayName;

@ActiveProfiles("ubuntu")
class EnvironmentVariablesTest {

  @Test
  @DisplayName("Test environment variables from ~/.bashrc")
  void testEnvironmentVariables() {

    System.out.println("Mysql User Username: " + System.getenv("MYSQL_USER_USERNAME"));
    System.out.println("Mysql User Password: " + System.getenv("MYSQL_USER_PASSWORD"));

    System.out.println("Mysql Admin Username: " + System.getenv("MYSQL_ADMIN_USERNAME"));
    System.out.println("Mysql Admin Password: " + System.getenv("MYSQL_ADMIN_PASSWORD"));

    System.out.println("H2 Username: " + System.getenv("H2_USERNAME"));
    System.out.println("H2 Password: " + System.getenv("H2_PASSWORD"));

    System.out.println("Mysql Root Username: " + System.getenv("MYSQL_ROOT_USERNAME"));
    System.out.println("Mysql Root Password: " + System.getenv("MYSQL_ROOT_PASSWORD"));

    System.out.println("OpenAI API Endpoint: " + System.getenv("OPENAI_API_ENDPOINT"));
    System.out.println("OpenAI API Key: " + System.getenv("OPENAI_API_KEY"));
  }
}
