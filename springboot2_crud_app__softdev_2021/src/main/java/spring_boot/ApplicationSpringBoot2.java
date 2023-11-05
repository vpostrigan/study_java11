package spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
@SpringBootApplication(scanBasePackages = {
        "spring_boot.entity",
        "spring_boot.repository",
        "spring_boot.service",
        "spring_boot.service.impl",
        "spring_boot.controller" })*/
@SpringBootApplication
public class ApplicationSpringBoot2 {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationSpringBoot2.class, args);
    }

}
