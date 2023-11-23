package com.joaovq.appsystem.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem-public")
            .pathsToMatch("/api/v1/customers/**", "/api/v1/credit/**")
            .build()
    }
}