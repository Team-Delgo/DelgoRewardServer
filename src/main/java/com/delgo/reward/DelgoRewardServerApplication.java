package com.delgo.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.delgo.reward.repository")
@EnableMongoRepositories(basePackages = "com.delgo.reward.mongoRepository")
public class DelgoRewardServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelgoRewardServerApplication.class, args);
    }

}
