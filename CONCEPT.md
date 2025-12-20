# Event-Driven Home Widgets Platform

## 1. Executive Summary

**Goal:**  
Imagine opening CHECK24 on your phone and quickly searching for flights to Munich. Later that evening, you open your laptop to continue your search. Instead of starting from scratch, you see personalized widgets showing relevant flights based on your earlier activity. That seamless, consistent experience across all devices is our goal.

Now zoom out: Each CHECK24 product operates as an independent system with its own data and operational limits. The Home page, however, receives traffic that can dwarf any individual product. If Home directly requested personalized widget content from dozens of backend products during peak load, it would accidentally become a traffic amplifier.

Ex: A single user opening the Home page could trigger 30+ backend calls. Now imagine this on peak times like Black Friday—the load would be catastrophic.


**Solution Overview:**  
This concept solves the problem through a decentralized contribution model with centralized delivery. Products contribute by publishing events and owning their widget logic (rules, content, and layout). Core provides a platform that consumes these events, generates widgets, and stores them in a fast, TTL-based cache. When Home renders on web, iOS, or Android, it reads user-specific widgets directly from cache which means no backend calls, no amplification, no cascading failures.


## 2. Problem Statement & Constraints

### 2.1 Decentralized Product Landscape
- CHECK24 products operate as independent systems ("speedboats" as you call it)
- No shared persistence layer
- Unknown traffic and performance characteristics per product

### 2.2 Home Page Requirements
- Extremely high and volatile traffic (Every User will access it at the start)
- First user touchpoint → business-critical
- Must remain available even if product systems fail

### 2.3 Key Constraints
- Home must never amplify product load
- Native apps cannot change layout without release
- Personalization must work cross-device
- Data freshness must be balanced with stability


## 3. High-Level Architecture

### 3.1 System Overview

```
Product Systems
   ↓ (domain & interaction events)
Redis Streams
   ↓
Widget Processor
   ├─ Rule Engine (product-owned)
   ├─ Widget Builder
   ├─ Optional AI Enrichment (async)
   ↓
Widget Cache (Redis, TTL)
   ↓
Home API (cache-only)
   ↓
Web / iOS / Android

```

### 3.2 Component Responsibilities

**Product Systems**
- Emit user events (actions done by the user)
- Own widget rules and personalization logic and schema for frontend
- Never queried synchronously by Home

**Widget Processor (Core)**
- Consumes events asynchronously
- Evaluates product-owned rules
- Writes widgets to cache
- Applies TTL and prioritization

**Widget Gateway**
- Reads widgets from cache
- Sorts and renders widgets
- Applies fallback logic

## 4. Event Model & Data Flow

### 4.1 Why Events Instead of APIs
- **Prevent load amplification:** Products publish events once, and Home reads from cache instead of calling many product APIs per page view.
- **Decouple availability:** Home stays available even if a product is slow or down because widgets are served from cache.
- **Allow async scaling:** Event processing scales independently with more consumers, without impacting Home latency.
- **Support cross-product personalization:** Events from multiple products build a shared user context without direct product-to-product calls.


### 4.2 Event Types

- **User Events (Product Events):**  
  Domain events emitted by decentralized product systems (e.g. searches, quotes, conversions) that drive widget creation, updates, and removal.

- **Widget Interaction Events (Analytics Events):**  
  Events emitted by Web and App clients (e.g. impressions, clicks) that measure widget performance and enable analytics, ranking, and optional AI-based enrichment.


### 4.3 User Events 

```json
{
  "userId": "string",
  "eventType": "INSURANCE_QUOTE",
  "productId": "insurance",
  "metadata": { },
  "timestamp": 1712345678
}
```

**Design Rationale:**
- Flexible metadata enables product autonomy
- Core does not interpret business semantics
- Events are immutable and append-only
### 4.4 Widget Interaction Events

In addition to product-generated user events, the platform captures widget interaction events
(e.g. impressions, clicks) emitted by Web and App clients.

These events are processed asynchronously and are **never used synchronously during Home rendering**.

Use cases include:
- Measuring widget performance and conversion
- Adjusting widget priority or TTL based on engagement
- Triggering optional AI-based content enrichment
- Supporting future A/B testing and ranking strategies

Widget interaction events follow the same event-driven principles as product events:
- No impact on Home latency
- No synchronous dependencies
- Failure-tolerant and optional
```
{
  "userId": "string",
  "widgetId": "string",
  "actionType": "IMPRESSION | CLICK",
  "timestamp": 1712345678
}
```

## 5. Widget Model & Rendering Contract

### 5.1 Widget Structure

```json
{
  "id": "string",
  "componentType": "card | banner | carousel",
  "schemaVersion": "v1",
  "payload": {
    "layout": { },
    "content": { }
  }
}
```

### 5.2 Rendering Philosophy
- Layout constraints enforced by platform
- Content and layout config fully dynamic
- Schema versioning prevents breaking changes
- Native apps render based on known component types

## 6. Rule Engine Design

### 6.1 Declarative Rules
- YAML-based rule definitions
- Owned by product teams
- No Core code changes required

### 6.2 Rule Evaluation Flow

    1. Event arrives
    2. Rules filtered by product + eventType
    3. Priority resolution
    4. Widget lifecycle action applied:
        - ADD_WIDGET
        - UPDATE_WIDGET
        - REMOVE_WIDGET

### 6.3 Benefits
- High flexibility
- Low coupling
- Easy experimentation
- Clear ownership

## 7. Personalization Strategy

- Event-driven personalization
- User-scoped widget cache
- Product-owned logic
- Optional AI enrichment (async, non-critical)

**Trade-off:**  
Eventual consistency accepted to guarantee availability.

## 8. Performance & High Availability

### 8.1 Cache-First Serving
- Home never blocks on processing
- Widgets served directly from Redis

### 8.2 Failure Handling
- Circuit breakers 
- Fallback widget service
- Dead-letter queues
- Retry with backoff

### 8.3 Graceful Degradation
- Partial widget availability allowed
- Home always renders

## 9. Cross-Device & Cross-Platform Consistency

- User-scoped widget state
- Shared cache across platforms
- Platform-specific rendering with shared data
- Logged-in users see consistent widgets

## 10. Deployment Concept (PoC)

### 10.1 Components
- Stateless services
- Redis (Streams + Cache)
- Horizontal scaling

### 10.2 PoC vs Production

| Aspect | PoC | Production |
|--------|-----|------------|
| Redis | Single node | Clustered |
| Consumers | Single | Multiple |
| Validation | Minimal | Strict |

## 11. Security & Data Protection

- No PII in events
- Metadata minimized
- TTL-based storage
- Product isolation by stream/topic

## 12. Decision Rationale & Trade-offs

### Key Decisions
<details>
<summary> Why Redis Streams instead of Kafka?</summary>
Redis Streams provide low-latency event processing and operational simplicity while being sufficient for short-lived personalization events, with the option to swap to Kafka later without changing producer contracts.
</details>

<details>
<summary> Why Eventual consistency instead of Real-Time Personaliztion?</summary>
Accepting eventual consistency allows the Home to remain highly available and performant under peak load, which is more critical for conversion than strictly real-time personalization.
</details>

<details>
<summary> Why Declarative rules?</summary>
Products can change widget logic without Core code changes. Descentrailization Principle.
</details>
<details>
<summary> Cache-first home</summary>
Serving the Home exclusively from cache guarantees constant latency and prevents load amplification, ensuring availability even when downstream systems fail.
</details>

### Trade-offs

| Decision | Benefit | Cost |
|----------|---------|------|
| Async processing | Stability | Slight delay |
| Flexible metadata | Product autonomy | Less strict typing |

## 13. Future Improvements

- Schema registry (New source to fetch new schemas)
- Feature flags
- ML-based ranking
- Rule DSL extensions

## 14. Summary

This concept enables a scalable, resilient, and flexible Home Widget platform that respects CHECK24's decentralized product structure while delivering a fast and personalized Home experience across all platforms.
