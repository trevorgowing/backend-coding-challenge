package com.trevorgowing.expenselist;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
@EnableJpaAuditing(modifyOnCreate = false)
public class Application {

  public static void main(String args[]) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Module jdk8Module() {
    return new Jdk8Module();
  }

  @Bean
  public Module javaTimeModule() {
    return new JavaTimeModule();
  }

  @Bean
  public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.dateFormat(new ISO8601DateFormat());
    return builder;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return Jackson2ObjectMapperBuilder.json().build();
  }
}
