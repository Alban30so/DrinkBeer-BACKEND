package fr.alban30so.dev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Value("${catalog.beer.api-key}")
    private String apiKey;

    @Bean
    public RestTemplate catalogBeerRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        //Auth API via clé API
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor(apiKey, "")
        );

        //accept header
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}