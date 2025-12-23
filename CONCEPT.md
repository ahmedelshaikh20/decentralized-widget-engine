# Event-Driven Home Widgets Platform

## 1. Intro
**Goal:** Deliver personalized Home widgets consistently across Web and native apps without calling product backends during rendering.

**Problem:** Home traffic can exceed any product’s capacity. Synchronous calls from Home to multiple products would amplify load and create cascading failures.

**Approach:** Products publish events. Core processes them asynchronously into per-user widgets stored in a fast cache. Home reads from cache only.

---
## 2. Problem Statement & Constraints

### 2.1 Decentralized Product Landscape
- CHECK24 products operate as independent units ("speedboats")
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

---
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

### 3.3 Public Contracts

#### 3.3.1 Home Widgets Read API (Core → Clients)
`GET /api/v1/widgets?platform=WEB|IOS|ANDROID`

- Auth: derives subjectId from session/JWT (PoC uses `userId` query param).
- Returns max **50 widgets**, ordered by `priority desc`.
- Widget payload size: max **32 KB** per widget (hard rejected at ingestion in production).
- Rendering compatibility:
  - Clients render by `componentType` + `schemaVersion`.
  - Unknown `componentType` is ignored (forward compatibility).

#### 3.3.2 Widget Interaction Events (Clients → Core)
`POST /api/v1/events/widget` with `WidgetEvent`.

- Asynchronous only.
- Used for analytics/ranking/AI enrichment, not required for correctness.


---
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
---
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
### 5.2 Rendering Contract (Schema Versioning & Native Safety)

Native apps cannot change UI code without a Store release. Therefore, widget rendering must be **forward-compatible**: if a client receives a widget it cannot render, it must **skip it safely** without crashing.

#### 5.2.1 Rendering Keys
Each widget is rendered using two fields:

- **`componentType`**: selects the renderer (e.g. `banner`, `card`, `carousel`)
- **`schemaVersion`**: selects the payload schema (e.g. `v1`)

#### 5.2.2 Version Gating Rule (All Clients)
Clients validate widget payloads before rendering:

- If the widget’s `schemaVersion` is not supported by the renderer, the widget is **ignored**.
- This guarantees that newly introduced schema versions do not break older app versions.

#### 5.2.3 Android Implementation (PoC)
Android uses typed mappers per component and schema version.  
Example: `WidgetPayload.toBannerV1Payload()`:

- Checks `schemaVersion == "v1"`
- Validates required fields (e.g. `title`)
- Returns a strongly typed payload (`BannerV1Payload`) or `null` if unsupported/invalid

If the mapper returns `null`, the widget is not rendered.

#### 5.2.4 Web Implementation (PoC)
Web maps `componentType` to a React component (e.g. `banner → BannerWidget`).  
To match native safety, web performs the same gating:
- If `schemaVersion` is unsupported for that component, the widget is skipped (or rendered via fallback).

#### 5.2.5 Layout Configuration Scope
To respect app release constraints, `layout` is **parametric configuration** (spacing, aspect ratios, typography hints), not an arbitrary UI tree.  
New `componentType`s or breaking schema changes require coordinated rollout (web deploy + native app update), while content updates remain dynamic.


---
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

### 6.4 Rule Distribution and Product Autonomy

To maintain the “speedboat” principle, rule changes must not require a Core deployment. In production, rules are therefore externalized and owned by product teams.

#### PoC Implementation
- Rules are loaded from the application classpath for simplicity and reproducibility.

#### Production Concept

**Externalized storage**
- Rules are stored outside Core deployments, e.g. in an object store (S3-compatible) or a dedicated configuration service.

**Self-service publishing**
- Each product publishes versioned rule bundles (YAML) to its own namespace (e.g. `rules/<productId>/<version>/rules.yaml`).

**Hot reloading**
- The Widget Processor refreshes rules without re-deploy:
  - via object store events (push-based)
- New rules are validated before activation; invalid bundles are rejected and the last known good version remains active.

**Isolation**
- Rule parsing/validation is performed per product namespace.
- A failure in one product’s rule bundle (e.g. “internet”) does not impact other products (e.g. “flight”); only that product’s rules are ignored until fixed.

---
## 7. Personalization Strategy

- Event-driven personalization
- User-scoped widget cache
- Product-owned logic
- Optional AI enrichment (async, non-critical)

**Trade-off:**  
Eventual consistency accepted to guarantee availability.
---
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
### 8.4 Cache Data Model (Redis)

To guarantee constant latency and avoid N+1 reads, personalized widgets are stored per user in a Redis HASH, with expiry tracked via a ZSET:

- `widgets:{subjectId}:hash` (HASH) → `widgetId -> widgetJson`
- `widgets:{subjectId}:exp`  (ZSET) → `widgetId -> expireAtEpochMs`
- `widgets:defaults` (STRING) → JSON array of default widgets

Read path (Home API):
1) Purge expired: `ZRANGEBYSCORE exp 0 now` → `HDEL` + `ZREM`
2) Load personalized: `HVALS widgets:{id}:hash`
3) Load defaults: `GET widgets:defaults`
4) Merge: personalized overrides defaults by widgetId

Complexity:
- **O(1) Redis calls** independent of number of widgets

### 8.5 Failure Modes

| Component | Failure scenario | User impact |   recovery |
|---|---|---|---|
| Product systems | Events stop being produced (or delayed) | No new personalization; existing widgets may become stale | Cached widgets remain until TTL; defaults continue to render; monitoring alerts on missing events |
| Stream ingestion (Redis Streams) | Stream unavailable | No new events processed | System continues serving cached widgets; once stream is back, processor resumes from consumer group offsets |
| Widget Processor | Processor down or lagging | Widgets not updated; expired widgets may persist until next purge | Horizontal scaling for consumers; retries + DLQ; lag metrics/alerts; safe restart resumes from consumer group |
| Redis cache | Cache unavailable / failover | Personalized widgets temporarily unavailable | Gateway serves critical fallback widgets; production uses Redis Sentinel/Cluster for automatic failover |
| Home API (Gateway) | Gateway unavailable | Home may miss widgets | Stateless multi-instance deployment behind LB; optional CDN-cached fallback response for extreme cases |

---

## 9. Cross-Device & Cross-Platform Consistency

- User-scoped widget state
- Shared cache across platforms
- Platform-specific rendering with shared data
- Logged-in users see consistent widgets
- Guest users use an anonymous, device-scoped `Id` (cookie/deviceId), so personalization is per-device until login
---
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

---
## 11. Security & Data Protection

- No PII in events
- Metadata minimized
- TTL-based storage
- Product isolation by stream/topic

---
## 12. Decision Rationale & Trade-offs

### Key Decisions
<details>
<summary> Why Redis Streams instead of Kafka?</summary>
Redis Streams provide low-latency event processing and operational simplicity while being sufficient for short-lived personalization events, with the option to swap to Kafka later without changing producer contracts.
</details>

<details>
<summary> Why Eventual consistency instead of Real-Time Personalization?</summary>
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

---
## 13. Future Improvements

- Schema registry 
- Feature flags
- ML-based ranking

---
## 14. Summary

This concept enables a scalable, resilient, and flexible Home Widget platform that respects CHECK24's decentralized product structure while delivering a fast and personalized Home experience across all platforms.
