package org.example.gateway.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

  private final ObjectMapper mapper;

  public JsonUtils(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public String toJson(Object value) {
    try {
      return mapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(value);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to serialize JSON", e);
    }
  }
}
