package com.delgo.reward.comm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SwaggerConfig.java
@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                        .components(new Components())
                        .info(apiInfo());
        }

        private Info apiInfo() {
                return new Info()
                        .title("Delgo Reward API")
                        .description("Springdoc을 사용한 Delgo Reward API 문서")
                        .version("1.1.1");
        }
}