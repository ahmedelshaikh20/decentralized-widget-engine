package org.example.widgetprocessor.config;

import io.github.sashirestela.openai.SimpleOpenAIDeepseek;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

  @Bean
  public SimpleOpenAIDeepseek openAIClient(
    @Value("${deepseek.api-key}") String apiKey
  ) {
    return SimpleOpenAIDeepseek.builder()
      .apiKey(apiKey)
      .build();
  }
}
