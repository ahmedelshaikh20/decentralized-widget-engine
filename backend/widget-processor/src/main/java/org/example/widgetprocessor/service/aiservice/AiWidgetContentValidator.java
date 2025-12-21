package org.example.widgetprocessor.service.aiservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Set;

@Component
public class AiWidgetContentValidator {

  private static final int MAX_TITLE_LENGTH = 80;
  private static final int MAX_SUBTITLE_LENGTH = 120;
  private static final int MAX_DESCRIPTION_LENGTH = 300;

  private static final Set<String> ALLOWED_URL_PREFIXES = Set.of(
    "/flight",
    "/insurance",
    "/mobile",
    "/internet",
    "/holiday"
  );

  private final ObjectMapper objectMapper;

  public AiWidgetContentValidator(@Qualifier("jsonObjectMapper") ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public AiWidgetContentSchema validate(String rawJson) {
    AiWidgetContentSchema content = parse(rawJson);

    validateRequiredFields(content);
    validateLengths(content);
    validateCta(content.cta());

    return content;
  }

  // ----------------- helpers -----------------

  private AiWidgetContentSchema parse(String rawJson) {
    try {
      return objectMapper.readValue(rawJson, AiWidgetContentSchema.class);
    } catch (Exception e) {
      throw new InvalidAiOutputException("Invalid JSON format", e);
    }
  }

  private void validateRequiredFields(AiWidgetContentSchema content) {
    if (content.title() == null || content.title().isBlank())
      throw new InvalidAiOutputException("Missing title");

    if (content.cta() == null)
      throw new InvalidAiOutputException("Missing CTA");

    if (content.cta().label() == null || content.cta().label().isBlank())
      throw new InvalidAiOutputException("Missing CTA label");

    if (content.cta().url() == null || content.cta().url().isBlank())
      throw new InvalidAiOutputException("Missing CTA url");
  }

  private void validateLengths(AiWidgetContentSchema content) {
    if (content.title().length() > MAX_TITLE_LENGTH)
      throw new InvalidAiOutputException("Title too long");

    if (content.subtitle() != null && content.subtitle().length() > MAX_SUBTITLE_LENGTH)
      throw new InvalidAiOutputException("Subtitle too long");

    if (content.description() != null && content.description().length() > MAX_DESCRIPTION_LENGTH)
      throw new InvalidAiOutputException("Description too long");
  }

  private void validateCta(AiWidgetContentSchema.Cta cta) {
    validateInternalUrl(cta.url());
  }

  private void validateInternalUrl(String url) {
    if (url.startsWith("http")) {
      throw new InvalidAiOutputException("External URLs are not allowed: " + url);
    }

    boolean allowed = ALLOWED_URL_PREFIXES
      .stream()
      .anyMatch(url::startsWith);

    if (!allowed) {
      throw new InvalidAiOutputException("URL not whitelisted: " + url);
    }

    try {
      URI.create(url);
    } catch (Exception e) {
      throw new InvalidAiOutputException("Invalid URL format");
    }
  }
}
