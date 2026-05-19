package com.avantdream.cloudpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.avantdream.cloudpulse.shared.config.AppProperties;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(AppProperties.class)
public class CloudpulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudpulseApplication.class, args);
    }
}
