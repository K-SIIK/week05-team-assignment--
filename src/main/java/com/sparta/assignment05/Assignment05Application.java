package com.sparta.assignment05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Assignment05Application {

    public static void main(String[] args) {
        SpringApplication.run(Assignment05Application.class, args);
    }

}
