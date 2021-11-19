package com.bemonovoid.playsqd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bemonovoid.playsqd")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
