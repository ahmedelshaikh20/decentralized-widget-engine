export const colors = {
  primary: '#2563eb',
  primaryHover: '#1d4ed8',
  primaryLight: '#eff6ff',
  white: '#ffffff',
  gray50: '#f9fafb',
  gray100: '#f3f4f6',
  gray200: '#e5e7eb',
  gray400: '#9ca3af',
  gray500: '#6b7280',
  gray600: '#4b5563',
  gray800: '#1f2937',
  red50: '#fef2f2',
  red200: '#fecaca',
  red800: '#991b1b'
};

export const styles = {
  button: {
    base: {
      padding: '0.75rem 1.5rem',
      borderRadius: '0.5rem',
      fontWeight: 500,
      border: 'none',
      cursor: 'pointer',
      transition: 'background-color 0.2s'
    },
    primary: {
      backgroundColor: colors.primary,
      color: colors.white
    },
    white: {
      backgroundColor: colors.white,
      color: colors.primary
    }
  },
  card: {
    base: {
      backgroundColor: colors.white,
      borderRadius: '1rem',
      padding: '1.5rem',
      transition: 'box-shadow 0.2s'
    },
    shadow: {
      default: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
      hover: '0 10px 15px -3px rgba(0, 0, 0, 0.1)'
    }
  }
};