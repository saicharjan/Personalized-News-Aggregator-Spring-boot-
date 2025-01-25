package com.newsaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class NewsAggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsAggregatorApplication.class, args);
    }
}
