# Developer Guideline: Building and Publishing Home Widgets

This document explains how decentralized product teams can contribute personalized Home Widgets to CHECK24 Home (Web + iOS + Android) without any synchronous dependency from Home to product backends.

---

## What you build (as a product team)

You contribute widgets by doing **two things**:

1. **Emit product user events** (e.g., SEARCH, QUOTE, BOOKING) to the core.
2. **Provide widget rules** (YAML) that map those events to widget creation/update/removal.

Home never calls your backend during rendering. Your events + rules drive widget generation asynchronously.

---

## 1)  Integration Model (Used in this PoC)

Products integrate via **Event + Rules**:
- Products emit user events.
- Products own YAML rules + widget templates.
- Core consumes events, evaluates rules, generates widgets, and serves them from Redis cache.

---

## 2) Product Event Contract (User Events)

### 2.1 Event shape

The platform expects **user events** with this logical structure:
```json
{
  "userId": "string",         
  "eventType": "INSURANCE_QUOTE",
  "productId": "insurance",
  "metadata": {},
  "timestamp": 1712345678
}
```

#### Field rules

- **userId**: in production this will be a pseudonymous subjectId derived from session/JWT. (PoC may accept raw userId.)
- **eventType**: stable string identifier, UPPER_SNAKE_CASE recommended.
- **productId**: your product key (e.g., flight, insurance, internet).
- **metadata**: optional map for personalization context (destination, category, lastAction, etc.).
- **timestamp**: epoch millis or seconds (be consistent; document it in your product).

### 2.2 Recommended event taxonomy

Keep event types small and stable. Examples:

- `HOME_VISIT`
- `SEARCH`
- `QUOTE_STARTED`
- `QUOTE_COMPLETED`
- `BOOKING_COMPLETED`
- `CONTRACT_ACTIVE`

### 2.3 Metadata guidelines

Metadata must be small, safe, and non-PII.

**Good:**
- `destination: "MUC"`
- `category: "SIM_ONLY"`
- `priceBucket: "LOW"`
- `intentScore: 0.8`

**Not allowed:**
- name, email, phone, address
- free-form text that could contain PII

---

## 3) Widget Rules (YAML)

Rules are product-owned configuration that maps events to widget lifecycle actions.

### 3.1 File naming and ownership

Each product publishes a rules bundle:

- **File:** `<productId>-rules.yaml`
- **Example:** `flight-rules.yaml`

In production, rules are stored externally (config service for ex) and hot reloaded.  
In the PoC, rules may be loaded from classpath.

### 3.2 Rule schema 

Each rule produces a widget with:

- `id` (stable widget id)
- `componentType` (banner, card, carousel)
- `schemaVersion` (v1, later v2, etc.)
- `layoutConfig` and `contentTemplate`
- `priority`, `ttlSeconds`, `platformVisibility`
- `action` (ADD_WIDGET, UPDATE_WIDGET, REMOVE_WIDGET)
- `trigger.eventTypes` (which product eventTypes activate this rule)

### 3.3 Example rule bundle
```yaml
product: flight

widgets:
  - id: flight-deals
    name: Flight Deals Banner
    schemaVersion: v1

    trigger:
      eventTypes: ["SEARCH", "HOME_VISIT"]

    componentType: banner
    action: ADD_WIDGET

    priority: 80
    ttlSeconds: 3600
    platformVisibility: ["WEB", "ANDROID", "IOS"]

    layoutConfig:
      style: "hero"

    contentTemplate:
      title: "Continue your flight search"
      subtitle: "Deals for {{destination}} are still available"
      imageUrl: "{{imageUrl}}"
      cta:
        label: "View flights"
        url: "/flight"
```

### 3.4 Template placeholders

`contentTemplate` supports simple placeholders:

- `{{destination}}` resolves from event metadata (if present)
- `{{history}}` may be available for advanced templates (optional)

If a placeholder is missing, it typically resolves to an empty string. Keep templates robust.

---

## 4) Widget Payload Contract (Client Rendering)

Home clients render widgets using:

- `componentType` to choose the renderer
- `schemaVersion` to validate payload compatibility

If a client cannot render a widget (unknown `componentType` or unsupported `schemaVersion`), it skips the widget safely.

### 4.1 Component types (PoC)

- `banner` (banner)
- `card` (simple card)
- `carousel` (horizontal list)

### 4.2 Schema versions

Start with `schemaVersion: v1`

Introduce `v2` only when both:
- Web supports it
- Native apps have released support (or you accept that older apps will skip it)

### 4.3 URL / CTA constraints

CTAs must link to internal paths only (no external http(s) URLs).

**Recommended:**
- `/flight`
- `/insurance`
- `/internet`
- `/mobile`
- `/holiday`

---

## 5) TTL, Freshness, and Widget Lifecycles

### 5.1 TTL selection guidelines

Choose TTL based on how quickly the underlying user intent expires:

- **High intent** (quote started / checkout): 10–60 minutes
- **Medium intent** (search): 1–6 hours
- **Low intent** (general browsing): 6–24 hours

### 5.2 Remove/Update patterns

Use lifecycle actions:

- `ADD_WIDGET`: create if missing
- `UPDATE_WIDGET`: replace content/layout for the same widgetId
- `REMOVE_WIDGET`: remove widget after a conversion or contract activation

---

## 6) Client Interaction Events (Analytics)

Clients emit widget interaction events:

- `IMPRESSION`
- `CLICK`
- `ITEM_CLICK`

These events:
- never block Home rendering
- are used for analytics/ranking/optional AI enrichment

### Endpoint
```
POST /api/v1/events/widget
```

### Body (WidgetEvent)
```json
{
  "userId": "string",
  "widgetId": "string",
  "widgetType": "banner|card|carousel",
  "actionType": "IMPRESSION|CLICK|ITEM_CLICK",
  "metadata": {}
}
```

---

## 7) Testing Your Integration

### 7.1 Local/dev test loop (recommended)

1. Start platform stack (gateway + processor + redis).
2. Emit a product event for a test user.
3. Call the Home Widgets API and verify widget output.

### 7.2 Fetch widgets
```bash
curl "http://<gateway-host>/api/v1/widgets?userId=123&platform=WEB"
```

### 7.3 Reset personalization (PoC only)
```bash
curl -X POST "http://<gateway-host>/api/v1/reset?userId=123"
```

### 7.4 Troubleshooting checklist

If widgets don't appear:

- Is the event being emitted with correct `productId` and `eventType`?
- Is `platformVisibility` including your platform? (Developer fall easily)
- Is TTL too small (widget expired)?
- Did `schemaVersion` mismatch cause the client to skip rendering?

---

## 8) Operational Expectations (Production)

###  Rollout strategy

- Start with v1 schemas and a small set of component types.
- Add new widget variants via rule updates (no app release required).
- Introduce new schema versions only with coordinated web + native support.

---

## 9) Ownership Model

### Product teams own:

- event emission semantics
- rule bundles + widget templates
- TTL + priorities per widget
- content quality and conversion goals

### Core platform owns:

- event ingestion + processing reliability
- caching
- schema validation and safety guardrails
- cross-platform rendering contract evolution

---

