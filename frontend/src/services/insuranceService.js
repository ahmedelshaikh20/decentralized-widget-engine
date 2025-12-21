import { API_CONFIG } from "../utils/constants";

export const insuranceService = {
  async homeVisit(userId) {
    return fetch(`${API_CONFIG.INSURANCE_API}/home`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId }),
    });
  },

  async createQuote(userId, type = 'health') {
    return fetch(`${API_CONFIG.INSURANCE_API}/quote`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId, type }),
    });
  },

  async signContract(userId, type = 'health') {
    return fetch(`${API_CONFIG.INSURANCE_API}/contract/sign`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId, type }),
    });
  },
};
