## 1. Executive Summary

**Goal:**  
Imagine opening CHECK24 on your phone and quickly searching for flights to Munich. Later that evening, you open your laptop to continue your search. Instead of starting from scratch, you see personalized widgets showing relevant flights based on your earlier activity. That seamless, consistent experience across all devices is our goal.

Now zoom out: Each CHECK24 product operates as an independent system with its own data and operational limits. The Home page, however, receives traffic that can dwarf any individual product. If Home directly requested personalized widget content from dozens of backend products during peak load, it would accidentally become a traffic amplifier.

Ex: A single user opening the Home page could trigger 30+ backend calls. Now imagine this on peak times like Black Friday—the load would be catastrophic.

**Solution Overview:**  
Products publish events and own widget logic (rules/templates). Core consumes events, generates widgets asynchronously, and stores them in a TTL-based cache. Home reads from cache only → no load amplification and no cascading failures.

---

## 2. Repository Deliverables

- **Concept:** `CONCEPT.md`
- **Developer guide:** `DEVELOPER_GUIDELINE.md`
- **Live PoC (Web Home):** http://165.22.27.127/?userId=400&userName=Ahmed
- **Live PoC (Widgets API):** http://165.22.27.127:8083/api/v1/widgets?userId=400&platform=WEB
  [![Video](https://img.youtube.com/vi/iPSvqACPiwo/hqdefault.jpg)](https://www.youtube.com/watch?v=iPSvqACPiwo)
---

## 3. PoC Summary

This PoC implements **(Event + Rules)**:

1) Product services emit **user events**
2) Core **Widget Processor** consumes events from **Redis Streams**
3) Processor evaluates **product-owned YAML rules** and builds widgets
4) Widgets are written to **Redis cache (per user)**
5) Home clients fetch from **Gateway (cache-only)**

---

## 4. Quick Demo

Fetch widgets:
```bash
curl "http://165.22.27.127:8083/api/v1/widgets?userId=400&platform=WEB"
````
## 5. Run Locally

```
docker compose up -d --build
```



