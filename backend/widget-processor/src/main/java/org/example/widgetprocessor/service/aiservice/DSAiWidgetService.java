package org.example.widgetprocessor.service.aiservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.SimpleOpenAIDeepseek;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.example.widgetprocessor.service.ruleservice.RuleEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DSAiWidgetService implements AiWidgetContentService {
  private static final Logger log = LoggerFactory.getLogger(RuleEngineService.class);

  private final SimpleOpenAIDeepseek aiGenerator;
  private final ObjectMapper mapper;
  private final AiWidgetContentValidator validator;

  public DSAiWidgetService(SimpleOpenAIDeepseek aiGenerator, @Qualifier("jsonObjectMapper") ObjectMapper mapper, AiWidgetContentValidator validator) {
    this.aiGenerator = aiGenerator;
    this.mapper = mapper;
    this.validator = validator;
  }

  public Map<String, Object> generateWidgetContent(
    String widgetId,
    List<Map<String, Object>> lastEvents
  ) {

    String prompt = buildPrompt(lastEvents);

    var request = ChatRequest.builder()
      .model("deepseek-chat")
      .message(ChatMessage.SystemMessage.of("You are a backend service. Respond ONLY with valid JSON. No markdown."))
      .message(ChatMessage.UserMessage.of(prompt))
      .temperature(0.7)
      .maxCompletionTokens(300)
      .build();

    var response = aiGenerator.chatCompletions().create(request).join();

    String json = response.firstContent();


    try {
      validator.validate(json);
      return mapper.readValue(json, Map.class);
    } catch (InvalidAiOutputException e) {
      log.warn("AI output rejected for widget {}: {}", widgetId, e.getMessage());
      // We should use fallback widget here but later do
      return Map.of();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  private String buildPrompt(List<Map<String, Object>> history) {
    return """
      You are a backend JSON generator for a homepage widget. Your response is parsed by a strict validator.
      Any output that is not a SINGLE valid JSON object will be rejected.

      Task:
      Generate personalized widget content based ONLY on the user's last 10 events.

      Context:
      - Events are ordered from MOST RECENT to OLDEST
      - Each event has: eventType, productId, timestamp, metadata
      - WidgetEvent fields:
          - userId (ignore, never mention)
          - widgetId (ignore, never mention)
          - widgetType (use to infer topic: flight/insurance/mobile/internet/holiday)
          - actionType (use to infer intent strength; CLICK > VIEW/IMPRESSION > DISMISS)
          - metadata (optional hints like productId, destination, plan, etc.)

      User events (JSON):
      %s

      Output requirements (ABSOLUTE):
      1) Output JSON ONLY. No prose, no markdown, no code fences, no comments.
      2) Return EXACTLY this object shape with EXACT keys (no extra keys):
         {
           "title": "string",
           "subtitle": "string",
           "description": "string",
           "cta": {
             "label": "string",
             "url": "string"
           }
         }
      3) Do NOT use null. All fields must be present and must be strings (cta is an object).
      4) Length limits:
         - title: max 80 chars (non-empty)
         - subtitle: max 120 chars (can be empty string)
         - description: max 300 chars (can be empty string)
      5) Text style:
         - Short, clear, user-facing
         - No emojis
         - No line breaks (single-line strings)
         - Avoid quotes inside text if possible

      CTA URL rules (CRITICAL):
      - The url MUST be EXACTLY ONE of the following values (choose one):
        "/flight"
        "/insurance"
        "/mobile"
        "/internet"
        "/holiday"
      - Do NOT add anything else: no extra path segments, no query params, no fragments.

      CTA selection:
      - Choose the CTA that best matches the MOST RECENT intent from events.
      - If unclear, use:
        label = "Explore offers"
        url   = "/holiday"

      Produce the final JSON object now.
      """
      .formatted(toJson(history));
  }


  private String toJson(Object value) {
    try {
      return mapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
