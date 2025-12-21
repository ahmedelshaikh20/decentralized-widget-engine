import { useState } from 'react';
import Navigation from './components/layout/Navigation';
import FlightsPage from './pages/FlightsPage';
import InsurancePage from './pages/InsurancePage';
import { HomePage } from './pages/HomePage';

export default function App() {
  const [activeTab, setActiveTab] = useState('home');

  const renderPage = () => {
    switch (activeTab) {
      case 'home':
        return <HomePage />;
      case 'flights':
        return <FlightsPage />;
      case 'insurance':
        return <InsurancePage />;
      default:
        return <HomePage />;
    }
  };

  return (
    <div style={{ 
      minHeight: '100vh', 
      backgroundColor: '#f9fafb' 
    }}>
      <Navigation 
        activeTab={activeTab} 
        onTabChange={setActiveTab} 
      />

      <main style={{ 
        maxWidth: '80rem', 
        margin: '0 auto', 
        padding: '2rem 1rem' 
      }}>
        {renderPage()}
      </main>
    </div>
  );
}