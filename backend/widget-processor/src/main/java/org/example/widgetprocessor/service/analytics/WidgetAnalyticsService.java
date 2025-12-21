package org.example.widgetprocessor.service.analytics;

import org.example.shared.models.events.WidgetEvent;
import org.example.shared.models.events.WidgetEventType;
import org.example.shared.models.widgets.Platform;
import org.example.shared.models.widgets.WidgetCacheDto;
import org.example.shared.models.widgets.WidgetPayload;
import org.example.widgetprocessor.repository.WidgetAnalyticsRepository;
import org.example.widgetprocessor.repository.WidgetCacheWriteRepository;
import org.example.widgetprocessor.service.aiservice.AiWidgetContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class WidgetAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(WidgetAnalyticsService.class);

    private final WidgetAnalyticsRepository analyticsRepo;
    private final AiWidgetContentService aiService;
    private final WidgetCacheWriteRepository widgetRepository;

    public WidgetAnalyticsService(
        WidgetAnalyticsRepository analyticsRepo,
        AiWidgetContentService aiService,
        WidgetCacheWriteRepository widgetRepository
    ) {
        this.analyticsRepo = analyticsRepo;
        this.aiService = aiService;
        this.widgetRepository = widgetRepository;
    }

    /**
     * Process widget interaction event
     */
    public void processWidgetInteraction(WidgetEvent event) {

        // 1. Store analytics data
        storeAnalytics(event);

        // 2. Track engagement metrics
        if (event.actionType() == WidgetEventType.CLICK) {
            analyticsRepo.incrementClickCount(event.userId(), event.widgetId());
        } else if (event.actionType() == WidgetEventType.IMPRESSION) {
            analyticsRepo.incrementImpressionCount(event.userId(), event.widgetId());
        }

        // 3. Check if AI generation should be triggered
        if (shouldTriggerAiGeneration(event)) {
            CompletableFuture.runAsync(() -> triggerAiWidgetGeneration(event.userId()));
        }
    }

    private void storeAnalytics(WidgetEvent event) {
        Map<String, Object> analyticsData = Map.of(
            "userId", event.userId(),
            "widgetId", event.widgetId(),
            "widgetType", event.widgetType(),
            "actionType", event.actionType().name(),
            "timestamp", System.currentTimeMillis(),
            "metadata", event.metadata()
        );

        analyticsRepo.storeWidgetEvent(event.userId(), analyticsData);
    }

    /**
     * AI generation triggers:
     * - Every 5th click
     * - Only for meaningful interactions (not impressions)
     */
    private boolean shouldTriggerAiGeneration(WidgetEvent event) {
        // Only trigger on clicks (meaningful engagement)
        if (event.actionType() != WidgetEventType.CLICK) {
            return false;
        }

        long clickCount = analyticsRepo.getUserClickCount(event.userId());

        // Generate AI widget every 5 clicks
        boolean shouldGenerate = clickCount % 5 == 0;

        if (shouldGenerate) {
            log.info("AI generation triggered for user {} (click count: {})",
                event.userId(), clickCount);
        }

        return shouldGenerate;
    }

    /**
     * Generate AI-powered widget based on user's interaction history
     */
    private void triggerAiWidgetGeneration(String userId) {
        try {
            // Get recent widget interactions
            List<Map<String, Object>> recentEvents = analyticsRepo.getRecentWidgetEvents(userId, 20);

            if (recentEvents.isEmpty()) {
                log.debug("No recent events for user {}, skipping AI generation", userId);
                return;
            }

            // Generate AI content
            Map<String, Object> aiContent = aiService.generateWidgetContent(
                "ai-personal-summary",
                recentEvents
            );

            if (aiContent.isEmpty()) {
                log.warn("AI service returned empty content for user {}", userId);
                return;
            }

            // Create AI widget
            WidgetCacheDto aiWidget = new WidgetCacheDto(
                "ai-personal-summary",
                "ai",
                "card",
                100, // High priority
                List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
                3600, // 1 hour TTL
                new WidgetPayload("v1", Map.of(), aiContent)
            );

            // Save to cache
            widgetRepository.saveWidget(userId, aiWidget);

            log.info("Generated AI widget for user {} based on {} interactions", userId, recentEvents.size());

        } catch (Exception e) {
            log.error("Failed to generate AI widget for user {}", userId, e);
        }
    }
}
