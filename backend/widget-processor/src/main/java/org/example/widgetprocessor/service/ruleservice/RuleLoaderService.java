package org.example.widgetprocessor.service.ruleservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.*;



// Think of it like a catalog = our roles catalog
@Service
public class RuleLoaderService {

  private static final Logger log = LoggerFactory.getLogger(RuleLoaderService.class);

  @Value("${processor.rules.directory:classpath:widget_rules/}")
  private String rulesDirectory;

  private final Map<String, List<WidgetRule>> rulesByProduct = new HashMap<>();
  private final List<WidgetRule> allRules = new ArrayList<>();

  private final ObjectMapper yamlMapper;

  public RuleLoaderService() {
    this.yamlMapper = new ObjectMapper(new YAMLFactory());
  }

  @PostConstruct
  public void loadRules() {
    try {
      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      Resource[] resources = resolver.getResources(rulesDirectory + "*.yaml");

      log.info("Loading widget rules from {}", rulesDirectory);
      log.info("Found {} YAML files", resources.length);

      for (Resource resource : resources) {
        try {
          String filename = Objects.requireNonNull(resource.getFilename());
          log.info("Processing file: {}", filename);

          String productId = filename.replace("-rules.yaml", "");

          RuleFileConfig wrapper =
            yamlMapper.readValue(resource.getInputStream(), RuleFileConfig.class);

          if (wrapper.widgets == null || wrapper.widgets.isEmpty()) {
            log.warn("No widgets found in {}", filename);
            continue;
          }

          for (WidgetRule rule : wrapper.widgets) {
            rule.setProductId(productId);
          }

          rulesByProduct.put(productId, wrapper.widgets);
          allRules.addAll(wrapper.widgets);

          log.info("Loaded {} rules for product '{}'", wrapper.widgets.size(), productId);

        } catch (Exception e) {
          log.error("Failed to load resource: {}", resource.getFilename(), e);
          throw e;
        }
      }

      log.info("Total rules loaded: {}", allRules.size());

    } catch (IOException e) {
      log.error("Error during rule loading", e);
    }
  }

  public List<WidgetRule> getRulesForProduct(String productId) {
    return rulesByProduct.getOrDefault(productId, Collections.emptyList());
  }

  public List<WidgetRule> getAllRules() {
    return allRules;
  }

  public static class RuleFileConfig {
    public String product;
    public List<WidgetRule> widgets;
  }
}
