package se.sprinta.headhunterbackend;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Log4j2
@SpringBootApplication
public class HeadhunterBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeadhunterBackendApplication.class, args);
    }

    /**
     * Prints out a message from application*.yml if it is being used for communication with the database.
     * The purpose for this is debugging.
     */

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return args ->
            log.info("Active Configuration File: " + environment.getProperty("active-application-yml"));
    }
}



