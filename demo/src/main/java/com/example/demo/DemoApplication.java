package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; // <-- Add this import

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // <-- Add this exclusion
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE); // <-- Disable the web server
        app.run(args);
    }
}