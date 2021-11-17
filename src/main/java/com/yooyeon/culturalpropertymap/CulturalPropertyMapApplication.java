package com.yooyeon.culturalpropertymap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CulturalPropertyMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CulturalPropertyMapApplication.class, args);
    }

}
