import { API_CONFIG } from "../utils/constants";

export function emitWidgetEvent({
  widgetId,
  widgetType,
  actionType,
  metadata = {}
}) {
  const payload = {
    userId: API_CONFIG.USER_ID,
    widgetId,
    widgetType,
    actionType, 
    metadata
  };

  // fire-and-forget (analytics must never block UI)
  fetch(`${API_CONFIG.BASE_URL}/events/widget`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  }).catch(() => {
    // intentionally ignored
  });
}

export default emitWidgetEvent;
