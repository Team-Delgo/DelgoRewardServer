package com.delgo.reward.comm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

// SwaggerConfig.java
@OpenAPIDefinition(
        info = @Info(title = "Delgo Reward API 명세서",
                description = "Delgo Reward API 명세서",
                version = "v1.0"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
}