import React from 'react';
import WidgetRenderer from '../components/widgets/WidgetRenderer';
import useWidgets from '../hooks/useWidgets';
import WidgetSkeleton from '../components/widgets/WidgetSkeleton';
import { API_CONFIG ,getUserFromUrl  } from '../utils/constants';

const getGreeting = (userName)=> {
  const hour = new Date().getHours();
  const name = userName ? `, ${userName}` : '';

  if (hour >= 5 && hour < 12) return `Guten Morgen${name} 🌅`;
  if (hour >= 12 && hour < 17) return `Guten Tag${name} ☀️`;
  if (hour >= 17 && hour < 22) return `Guten Abend${name} 🌙`;
  return `Willkommen${name} 👋`;
};

const { userId, userName } = getUserFromUrl();


// Reset Button Component
const ResetButton = ({ onClick }) => (
  <button
    onClick={onClick}
    style={{
      backgroundColor: '#2563eb',
      color: 'white',
      padding: '0.75rem 1.5rem',
      borderRadius: '0.5rem',
      fontWeight: 500,
      width: 'fit-content',
      border: 'none',
      cursor: 'pointer',
      transition: 'background-color 0.2s',
      whiteSpace: 'nowrap',
      flexShrink: 0
    }}
    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#1d4ed8'}
    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#2563eb'}
  >
    Reset Widgets 
  </button>
);

// Header Component
const PageHeader = ({ onReset }) => (
  <div style={{
    marginBottom: '2rem',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: '1.5rem'
  }}>
    <h1 style={{
      fontSize: '2.25rem',
      fontWeight: 'bold',
      color: '#1f2937',
      margin: 0
    }}>
      {getGreeting(userName)}
    </h1>
    <ResetButton onClick={onReset} />
  </div>
);

// Loading State Component
const LoadingState = () => (
  <div style={{
    display: 'flex',
    flexDirection: 'column',
    gap: '1.5rem'
  }}>
    {[1, 2, 3].map(i => (
      <WidgetSkeleton key={i} />
    ))}
  </div>
);

// Error State Component
const ErrorState = ({ message }) => (
  <div style={{
    backgroundColor: '#fef2f2',
    border: '1px solid #fecaca',
    borderRadius: '0.5rem',
    padding: '1.5rem',
    marginBottom: '1.5rem'
  }}>
    <p style={{
      color: '#991b1b',
      margin: 0
    }}>
      {message}
    </p>
  </div>
);

// Empty State Component
const EmptyState = () => (
  <div style={{
    textAlign: 'center',
    padding: '5rem 1rem'
  }}>
    <div style={{
      fontSize: '4rem',
      marginBottom: '1.5rem'
    }}>
      📭
    </div>
    <p style={{
      color: '#6b7280',
      fontSize: '1.125rem',
      fontWeight: 500,
      margin: 0,
      marginBottom: '0.75rem'
    }}>
      No widgets available
    </p>
    <p style={{
      color: '#9ca3af',
      fontSize: '0.875rem',
      margin: 0
    }}>
      Try searching for flights to see personalized widgets
    </p>
  </div>
);

// Widgets List Component
const WidgetsList = ({ widgets }) => (
  <div style={{
    display: 'flex',
    flexDirection: 'column',
    gap: '1.5rem'
  }}>
    {widgets.map(widget => (
      <WidgetRenderer key={widget.id} widget={widget} />
    ))}
  </div>
);

// Main HomePage Component
export const HomePage = () => {
  const { widgets, loading, error } = useWidgets();

  const handleResetWidgets = async () => {
    try {
      await fetch(
       `${API_CONFIG.BASE_URL}/reset?userId=${userId}`,
        { method: 'POST' }
      );
      window.location.reload();
    } catch (err) {
      console.error('Failed to reset widgets:', err);
    }
  };

  return (
    <main style={{
      maxWidth: '80rem',
      margin: '0 auto',
      padding: '2rem 1rem'
    }}>
      <PageHeader onReset={handleResetWidgets} />

      {loading && <LoadingState />}
      
      {error && <ErrorState message={error} />}
      
      {!loading && !error && (
        widgets.length === 0 ? <EmptyState /> : <WidgetsList widgets={widgets} />
      )}
    </main>
  );
};