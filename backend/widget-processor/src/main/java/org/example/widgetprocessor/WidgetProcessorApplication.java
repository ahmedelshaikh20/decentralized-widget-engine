package org.example.widgetprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WidgetProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(WidgetProcessorApplication.class, args);
  }

}
