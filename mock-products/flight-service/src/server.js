import express from "express";
import cors from "cors";
import { redis } from "./redis.js";

const app = express();

/* -------------------- Middleware -------------------- */
app.use(express.json());
app.use(
  cors({
    origin: "*",
    methods: ["GET", "POST"],
    allowedHeaders: ["Content-Type"]
  })
);

/* -------------------- Insurance Helpers -------------------- */

function insuranceImage(type) {
  const images = {
    health: "https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=800&q=80",
    car: "https://images.unsplash.com/photo-1502877338535-766e1452684a?w=800&q=80", 
    travel: "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800&q=80", 
    life: "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=800&q=80",
    home: "https://images.unsplash.com/photo-1560518883-ce09059eeffa?w=800&q=80",
    pet: "https://images.unsplash.com/photo-1450778869180-41d0601e046e?w=800&q=80"
  };
  
  return images[type.toLowerCase()] || images.health;
}


/* -------------------- Constants -------------------- */
const PORT = process.env.PORT || 9001;
const STREAM_KEY = process.env.STREAM_KEY || "user-events";

/* -------------------- Redis Stream Publisher -------------------- */
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

  console.log(`[stream] ${eventType} for user ${userId} -> ${JSON.stringify(metadata || {})}`);
  return id;
}

/* -------------------- Helpers -------------------- */

function pickImage(region) {
  if (region === "Spain") {
    return "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=1200&q=80";
  }
  return "https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=1200&q=80";
}

function getDestinationImage(code) {
  const images = {
    BCN: "https://images.unsplash.com/photo-1562883676-8c7feb83f09b?w=800&q=80",
    MAD: "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=800&q=80",
    PMI: "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800&q=80",
    IBZ: "https://images.unsplash.com/photo-1564415315949-7a0c4c73aab4?w=800&q=80",
    PAR: "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800&q=80",
    ROM: "https://images.unsplash.com/photo-1526481280691-90652ae4f8c7?w=800&q=80"
  };

  return images[code] || images.BCN;
}




function buildDeals({ from, region, budget }) {
  const destinations =
    region === "Spain"
      ? ["BCN", "MAD", "PMI", "IBZ"]
      : ["PAR", "ROM"];

  return destinations.map((to) => ({
    title: `${from} → ${to} from €${budget === "low" ? 39 : 79}`,
    imageUrl: getDestinationImage(to),
    iconUrl: "https://cdn-icons-png.flaticon.com/512/2830/2830284.png",
    ctaLabel: "Book Now",
    ctaUrl: `/flight/search?from=${from}&to=${to}`
  }));
}

/* -------------------- Personalization -------------------- */

async function buildFlightSearchMetadata({ userId, from, to }) {
  const profile = await redis.hgetall(`user:${userId}:profile`);

  const homeAirport = profile.homeAirport || from;
  const preferredRegion = profile.preferredRegion || "Spain";
  const budget = profile.budget || "medium";

  const minPrice =
    budget === "low"
      ? Math.floor(Math.random() * 50) + 29
      : Math.floor(Math.random() * 100) + 59;

  return {
    from: homeAirport,
    to,
    minPrice,
    imageUrl: pickImage(preferredRegion),
    deals: buildDeals({
      from: homeAirport,
      region: preferredRegion,
      budget
    })
  };
}

/* -------------------- Routes -------------------- */


// HOME VISIT 
app.post("/api/flight/home", async (req, res) => {
  const { userId } = req.body;

  try {
    const metadata = {
      imageUrl: pickImage("Spain"),
      preferredRegion: "Spain",
    };

    const id = await publishEvent({
      userId,
      eventType: "HOME_VISIT",
      productId: "flight",
      metadata,
    });

    res.json({ status: "ok", eventId: id, metadata });
  } catch (e) {
    res.status(400).json({ status: "error", message: e.message });
  }
});




// Flight search
app.post("/api/flight/search", async (req, res) => {
  const { userId, from = "MUC", to = "BCN" } = req.body;

  try {
    const metadata = await buildFlightSearchMetadata({
      userId,
      from,
      to
    });

    const id = await publishEvent({
      userId,
      eventType: "FLIGHT_SEARCH",
      productId: "flight", 
      metadata
    });

    res.json({ status: "ok", eventId: id });
  } catch (err) {
    res.status(400).json({ status: "error", message: err.message });
  }
});

// Flight booked
app.post("/api/flight/book", async (req, res) => {
  const { userId, to = "BCN" } = req.body;

  try {
    const id = await publishEvent({
      userId,
      eventType: "FLIGHT_BOOKED",
      productId: "flight",
      metadata: {
        bookingId: "A15X903",
        flightNumber: "LH1823",
        departureDate: "2025-04-11",
        to,
      }
    });

    res.json({ status: "ok", eventId: id });
  } catch (err) {
    res.status(400).json({ status: "error", message: err.message });
  }
});



app.post("/api/insurance/quote", async (req, res) => {
  const { userId, type = "health" } = req.body;

  try {
    const metadata = {
      type,
      quoteId: `Q-${Math.floor(Math.random() * 100000)}`,
      imageUrl: insuranceImage(type)
    };

    const id = await publishEvent({
      userId,
      eventType: "INSURANCE_QUOTE",
      productId: "insurance",
      metadata
    });

    res.json({ status: "ok", eventId: id, metadata });
  } catch (e) {
    res.status(400).json({ error: e.message });
  }
});

app.post("/api/insurance/contract/sign", async (req, res) => {
  const { userId, type = "health" } = req.body;

  try {
    const metadata = {
      type,
      contractId: `C-${Math.floor(Math.random() * 100000)}`,
      expiryDate: "2026-03-31",
      imageUrl: insuranceImage(type)
    };

    const id = await publishEvent({
      userId,
      eventType: "CONTRACT_SIGNED",
      productId: "insurance",
      metadata
    });

    res.json({ status: "ok", eventId: id });
  } catch (e) {
    res.status(400).json({ error: e.message });
  }
});

app.post("/api/insurance/home", async (req, res) => {
  const { userId } = req.body;

  console.log("INSURANCE HOME VISIT HIT", userId);
  try {
    const id = await publishEvent({
      userId,
      eventType: "HOME_VISIT",
      productId: "insurance",
      metadata: {}
    });

    res.json({ status: "ok", eventId: id });
  } catch (e) {
    res.status(400).json({ error: e.message });
  }
});



app.listen(PORT, () => {
  console.log(`Mock Flight Service running on port ${PORT}`);
});
