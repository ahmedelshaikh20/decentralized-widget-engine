import express from "express";
import { redis } from "./redis.js";

const app = express();
app.use(express.json());

const PORT = process.env.PORT || 9001;
const STREAM_KEY = process.env.STREAM_KEY || "user-events";

async function publishEvent({ userId, eventType, productId, metadata }) {
  if (!userId || !eventType || !productId) {
    throw new Error("userId, eventType and productId are required");
  }

  const id = await redis.xadd(
    STREAM_KEY,
    "*",
    "userId", userId,
    "eventType", eventType,
    "productId", productId,
    "metadata", JSON.stringify(metadata || {}),
    "timestamp", Date.now().toString()
  );

  console.log(`[stream] Published ${eventType} for user ${userId} with id ${id}`);
  return id;
}

app.post("/api/flight/search", async (req, res) => {
  const { userId, from, to, date } = req.body || {};

  try {
    const id = await publishEvent({
      userId,
      eventType: "FLIGHT_SEARCH",
      productId: "flight",
      metadata: { from, to, date }
    });

    res.json({
      status: "ok",
      message: "Flight search event published",
      eventId: id
    });
  } catch (err) {
    console.error(err);
    res.status(400).json({ status: "error", message: err.message });
  }
});


app.post("/api/flight/book", async (req, res) => {
  const { userId, bookingId, price } = req.body || {};

  try {
    const id = await publishEvent({
      userId,
      eventType: "FLIGHT_BOOKED",
      productId: "flight",
      metadata: { bookingId, price }
    });

    res.json({
      status: "ok",
      message: "Flight booked event published",
      eventId: id
    });
  } catch (err) {
    console.error(err);
    res.status(400).json({ status: "error", message: err.message });
  }
});

app.listen(PORT, () => {
  console.log(`✈️  Mock Flight Service running on port ${PORT}`);
  console.log(`   POST http://localhost:${PORT}/api/flight/search`);
  console.log(`   POST http://localhost:${PORT}/api/flight/book`);
});