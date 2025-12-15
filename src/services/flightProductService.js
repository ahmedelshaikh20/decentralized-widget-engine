import { API_CONFIG } from "../utils/constants";

export const flightProductService = {
  async searchFlights(userId, from, to, date) {
    return fetch(`${API_CONFIG.FLIGHT_API}/search`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, from, to, date })
    });
  },

  async bookFlight(userId, to) {
    return fetch(`${API_CONFIG.FLIGHT_API}/book`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, to })
    });
  }
};