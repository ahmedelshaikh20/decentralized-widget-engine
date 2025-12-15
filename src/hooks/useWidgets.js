import { useState, useEffect } from 'react';
import { API_CONFIG } from '../utils/constants';

import { useCallback } from 'react';

export default function useWidgets() {
  const [widgets, setWidgets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchWidgets = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const res = await fetch(
        `${API_CONFIG.BASE_URL}/widgets?userId=${API_CONFIG.USER_ID}&platform=${API_CONFIG.PLATFORM}`
      );

      if (!res.ok) throw new Error('Failed loading API');

      const data = await res.json();
      setWidgets(data.widgets ?? []);
    } catch {
      setError('Failed to load widgets');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchWidgets();
  }, [fetchWidgets]);

  return { widgets, loading, error, refetch: fetchWidgets };
}
