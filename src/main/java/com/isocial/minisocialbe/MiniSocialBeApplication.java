package com.isocial.minisocialbe;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class    MiniSocialBeApplication {

    public static void main(String[] args) {
        // Load .env file if exists (for local development)
        // In production (Docker), environment variables are passed directly
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            System.out.println("No .env file found, using environment variables from system");
        }
        SpringApplication.run(MiniSocialBeApplication.class, args);
    }

}
