package org.example.widgetprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WidgetProcessorApplicationTests {

  @Test
  void contextLoads() {
  }

    @Autowired
    StringRedisTemplate redis;

    @Test
    void testConnection() {
      redis.opsForValue().set("processor:test", "ok");
      String value = redis.opsForValue().get("processor:test");

      assertEquals("ok", value);
    }


}
