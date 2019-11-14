package com.thanhld.server959.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.problem.ProblemModule;

@Configuration
public class ZalandoConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
        return (Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder)  ->
                jacksonObjectMapperBuilder.modules(new ProblemModule().withStackTraces(false));
    }
}
