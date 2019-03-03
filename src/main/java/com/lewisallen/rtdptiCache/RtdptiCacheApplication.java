package com.lewisallen.rtdptiCache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ThymeleafProperties.class)
public class RtdptiCacheApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RtdptiCacheApplication.class, args);

        new AppConfig();
    }
}
