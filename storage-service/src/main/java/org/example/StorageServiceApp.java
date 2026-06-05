package org.example;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class StorageServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApp.class, args);
        System.out.println("Spring Boot application is ready");
    }
}
