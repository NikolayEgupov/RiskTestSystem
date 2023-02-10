package ru.egupov.risktestsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Properties;

@SpringBootApplication
public class RiskTestSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskTestSystemApplication.class, args);
    }

    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
