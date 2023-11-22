package com.example.fastlms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class FastlmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastlmsApplication.class, args);
    }

}
