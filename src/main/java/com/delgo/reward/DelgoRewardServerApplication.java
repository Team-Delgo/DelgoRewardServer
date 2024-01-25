package com.delgo.reward;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@OpenAPIDefinition(servers = {@Server(url = "https://www.test.delgo.pet", description = "Dev Server")})
@SpringBootApplication
public class DelgoRewardServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelgoRewardServerApplication.class, args);
    }
}
