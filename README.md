# Distributed API Rate Limiter Ecosystem

A highly scalable, production-grade distributed rate-limiting architecture designed to protect downstream services from abuse. 

This ecosystem utilizes a **Java Spring Boot** gateway component implementing the **Token Bucket algorithm via Redis**, paired with an asynchronous logging pipeline powered by **Apache Kafka**, **Node.js**, and **MongoDB**.



## 🏗️ Architecture Overview

1. **Rate Limiting Layer (Spring Boot + Redis):** Intercepts incoming HTTP requests, extracts the `X-User-ID` header, and evaluates rate limits against a Redis-backed Token Bucket.
2. **Messaging Layer (Kafka):** Non-blocking emission of rate-limit metrics and access logs to a `rate-limit-logs` topic.
3. **Ingestion Layer (Node.js):** A lightweight consumer service that pulls logs from Kafka in batches.
4. **Storage Layer (MongoDB):** Time-series optimized storage for rate-limiting analytics and audit trails.

