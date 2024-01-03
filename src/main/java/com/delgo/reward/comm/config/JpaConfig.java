package com.delgo.reward.comm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.delgo.reward")
@EnableMongoRepositories(basePackages = "com.delgo.reward")
public class JpaConfig {
}