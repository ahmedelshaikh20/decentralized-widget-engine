import Redis from "ioredis";
import dotenv from "dotenv";

dotenv.config();

export const redis = new Redis({
  host: process.env.REDIS_HOST || "localhost",
  port: Number(process.env.REDIS_PORT || 6379),
  password: process.env.REDIS_PASSWORD || undefined
});

redis.on("connect", () => {
  console.log("[redis] Connected to Redis");
});

redis.on("error", (err) => {
  console.error("[redis] Error:", err);
});