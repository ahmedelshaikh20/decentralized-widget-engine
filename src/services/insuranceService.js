import { API_CONFIG } from "../utils/constants";

// To Complete Later: Add Proper Response in the Server
export const insuranceService = {
  async compare(userId) {
    return await fetch(`http://localhost:9002/api/insurance/compare`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId })
    });
  },

  async purchase(userId) {
    return await fetch(`http://localhost:9002/api/insurance/purchase`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId })
    });
  }
};
