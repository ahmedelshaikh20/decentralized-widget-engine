package org.example.widgetprocessor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    StringRedisTemplate template = new StringRedisTemplate(connectionFactory);

    // Keys + Values stored as plain UTF-8 strings
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());

    template.afterPropertiesSet();
    return template;
  }


    //This bean to read json files
    @Bean
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }


    //This bean to read yaml files
    @Bean
    public ObjectMapper yamlObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }
}
