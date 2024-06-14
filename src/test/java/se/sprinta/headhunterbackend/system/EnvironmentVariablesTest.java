package se.sprinta.headhunterbackend.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-mysql")
public class EnvironmentVariablesTest {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Test
    void testMySQLTestDatabaseEnvironmentVariable() {
        Assertions.assertEquals(datasourceUrl,
                "jdbc:mysql://localhost:3306/headhunter-db-test?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin");
    }
}
