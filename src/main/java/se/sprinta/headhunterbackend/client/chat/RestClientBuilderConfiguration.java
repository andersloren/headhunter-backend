package se.sprinta.headhunterbackend.client.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientBuilderConfiguration {

    /**
     * Replace the autoconfigured RestClient.Builder bean which defaults to a SimpleClientHttpRequestFactory.
     * This bean will use JdkClientHttpRequestFactory.
     *
     * @return RestClient.Builder
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory());
    }

}
