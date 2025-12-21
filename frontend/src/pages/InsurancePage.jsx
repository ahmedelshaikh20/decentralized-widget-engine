import React, { useState } from "react";
import { insuranceService } from "../services/insuranceService";
import { API_CONFIG } from "../utils/constants";

// Simple Action Button (reuse from above)
const InsuranceButton = ({ onClick, disabled, icon, label, variant = "blue" }) => {
  const colors = {
    blue: { bg: '#2563eb', hover: '#1d4ed8' },
    green: { bg: '#16a34a', hover: '#15803d' }
  };

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      style={{
        width: '100%',
        backgroundColor: colors[variant].bg,
        color: 'white',
        padding: '1rem 1.5rem',
        borderRadius: '0.75rem',
        fontWeight: 600,
        border: 'none',
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.5 : 1,
        transition: 'all 0.2s',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '0.75rem',
        fontSize: '1rem'
      }}
      onMouseEnter={(e) => {
        if (!disabled) e.currentTarget.style.backgroundColor = colors[variant].hover;
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.backgroundColor = colors[variant].bg;
      }}
    >
      <span style={{ fontSize: '1.5rem' }}>{icon}</span>
      <span>{label}</span>
    </button>
  );
};

export default function InsurancePage() {
  const [loading, setLoading] = useState(false);

  const triggerCompare = async () => {
    setLoading(true);
    try {
      await insuranceService.signContract(API_CONFIG.USER_ID);
      setTimeout(() => window.location.reload(), 500);
    } catch (error) {
      console.error("Sign contract failed:", error);
      setLoading(false);
    }
  };

  const triggerPurchase = async () => {
    setLoading(true);
    try {
      await insuranceService.createQuote(API_CONFIG.USER_ID);
      setTimeout(() => window.location.reload(), 500);
    } catch (error) {
      console.error("Create quote failed:", error);
      setLoading(false);
    }
  };

  // add this in InsurancePage
const triggerHomeVisit = async () => {
  setLoading(true);
  try {
    await insuranceService.homeVisit(API_CONFIG.USER_ID);
    setTimeout(() => window.location.reload(), 500);
  } catch (error) {
    console.error("Home visit failed:", error);
    setLoading(false);
  }
};


  return (
    <div style={{
      maxWidth: '600px',
      margin: '0 auto',
      padding: '2rem 1rem'
    }}>
      {/* Card */}
      <div style={{
        backgroundColor: 'white',
        borderRadius: '1rem',
        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        padding: '2rem'
      }}>
        {/* Header */}
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🛡️</div>
          <h1 style={{
            fontSize: '1.875rem',
            fontWeight: 'bold',
            color: '#1f2937',
            margin: '0 0 0.5rem 0'
          }}>
            Insurance Simulator
          </h1>
          <p style={{ color: '#6b7280', fontSize: '0.875rem', margin: 0 }}>
            Test insurance comparison and purchase events
          </p>
        </div>

        {/* Insurance Types */}
        <div style={{
          backgroundColor: '#f0fdf4',
          borderRadius: '0.75rem',
          padding: '1rem',
          marginBottom: '1.5rem',
          textAlign: 'center'
        }}>
          <p style={{
            color: '#1f2937',
            fontSize: '0.875rem',
            fontWeight: 600,
            margin: 0
          }}>
            🏥 Health | 🚗 Auto | 🏠 Home
          </p>
        </div>

        {/* Buttons */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {/* add this button in the Buttons section (above/below others) */}
          <InsuranceButton
            onClick={triggerHomeVisit}
            disabled={loading}
            icon="🏠"
            label="Simulate Home Visit"
            variant="blue"
          />

          <InsuranceButton
            onClick={triggerCompare}
            disabled={loading}
            icon="📊"
            label="Simulate Compare Insurance"
            variant="blue"
          />
          <InsuranceButton
            onClick={triggerPurchase}
            disabled={loading}
            icon="🛒"
            label="Simulate Purchase Insurance"
            variant="green"
          />
        </div>

        {/* Loading */}
        {loading && (
          <div style={{
            marginTop: '1.5rem',
            textAlign: 'center',
            color: '#6b7280',
            fontSize: '0.875rem'
          }}>
            ⏳ Processing...
          </div>
        )}
      </div>
    </div>
  );
}