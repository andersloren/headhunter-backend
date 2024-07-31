package se.sprinta.headhunterbackend.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EnvironmentVariablesTest {

    @Value("${MYSQL_USERNAME}")
    private String mysqlUsername;

    @Value("${MYSQL_PASSWORD}")
    private String mysqlPassword;

    @Test
    void test_EnvironmentVariables() {
        System.out.println("MySQL_Username :" + mysqlUsername);
        System.out.println("MySQL_Password :" + mysqlPassword);
    }
}
