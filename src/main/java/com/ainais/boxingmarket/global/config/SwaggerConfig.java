package com.ainais.boxingmarket.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Boxing Market API")
                        .description("DDD 아키텍처 실습을 위한 커머스 API 문서입니다.")
                        .version("1.0.0"));
    }
}