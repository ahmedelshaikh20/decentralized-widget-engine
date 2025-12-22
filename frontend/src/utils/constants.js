export const getUserFromUrl = () => {
  const params = new URLSearchParams(window.location.search);

  return {
    userId: params.get('userId') || '123',
    userName: params.get('userName') || 'Ahmed',
  };
};

export const USER_CONTEXT = getUserFromUrl();

export const API_CONFIG = {
  BASE_URL: 'http://165.22.27.127:8083/api/v1',
  FLIGHT_API: 'http://165.22.27.127:9001/api/flight',
  INSURANCE_API: 'http://165.22.27.127:9001/api/insurance',
  USER_ID: USER_CONTEXT.userId,
  PLATFORM: 'WEB',
};

export const NAV_ITEMS = [
  { id: 'home', label: 'Home', icon: 'Home' },
  { id: 'flights', label: 'Flights', icon: 'Plane' },
  { id: 'insurance', label: 'Insurance', icon: 'Shield' },
];
