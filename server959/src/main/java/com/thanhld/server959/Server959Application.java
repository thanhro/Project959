package com.thanhld.server959;

import com.thanhld.server959.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Server959Application {

    public static void main(String[] args) {
        SpringApplication.run(Server959Application.class, args);
    }
}
