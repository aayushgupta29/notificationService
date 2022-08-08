package com.meesho.notificationservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        System.out.println("in main class");
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    private static final Logger LOGGER = LogManager.getLogger(NotificationServiceApplication.class);

    @Bean
    public RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);
        LOGGER.info("Rest Template has been initialised!");

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        return restTemplate;
    }


}
