package com.twitchcollector.callbackconsumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
public class Config {

    @Bean
    public Supplier<String> uuidSupplier() {
        return () -> UUID.randomUUID().toString();
    }

    @Bean
    public Supplier<String> timeSupplier() {
        return () -> Instant.now().toString();
    }

    @Bean
    public Supplier<Instant> instantSupplier() {
        return Instant::now;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(NON_NULL).registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
    }
}
