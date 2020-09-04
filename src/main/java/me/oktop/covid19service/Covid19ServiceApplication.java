package me.oktop.covid19service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Covid19ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19ServiceApplication.class, args);
    }

}
