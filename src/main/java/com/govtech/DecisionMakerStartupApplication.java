package com.govtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = {
        "com.govtech.*"})
@EnableTransactionManagement
public class DecisionMakerStartupApplication {

    public static void main(String[] args) {

        SpringApplication.run(DecisionMakerStartupApplication.class, args);

    }
   

}
