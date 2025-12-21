import React from 'react';
import { Home, Plane, Shield, Wifi, DollarSign } from 'lucide-react';
import { NAV_ITEMS } from '../../utils/constants';
import { colors } from '../../styles/styles';

const iconMap = { Home, Plane, Shield, Wifi, DollarSign };

const Navigation = ({ activeTab, onTabChange }) => {
  return (
    <nav style={{
      backgroundColor: colors.white,
      borderBottom: `1px solid ${colors.gray200}`,
      position: 'sticky',
      top: 0,
      zIndex: 50,
      boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.1)'
    }}>
      <div style={{
        maxWidth: '80rem',
        margin: '0 auto',
        padding: '0 1rem'
      }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          height: '64px'
        }}>
          {/* Logo */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <div style={{
              width: '40px',
              height: '40px',
              backgroundColor: colors.primary,
              borderRadius: '8px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}>
              <span style={{ color: colors.white, fontWeight: 'bold', fontSize: '1.25rem' }}>
                C24
              </span>
            </div>
            <span style={{ fontWeight: 'bold', fontSize: '1.25rem', color: colors.gray800 }}>
              CHECK24
            </span>
          </div>

          {/* Navigation Items */}
          <div style={{ display: 'flex', gap: '0.25rem', alignItems: 'center' }}>
            {NAV_ITEMS.map(item => {
              const Icon = iconMap[item.icon];
              const isActive = activeTab === item.id;
              
              return (
                <button
                  key={item.id}
                  onClick={() => onTabChange(item.id)}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.5rem',
                    padding: '0.5rem 1rem',
                    borderRadius: '0.5rem',
                    border: 'none',
                    cursor: 'pointer',
                    transition: 'all 0.2s',
                    backgroundColor: isActive ? colors.primaryLight : 'transparent',
                    color: isActive ? colors.primary : colors.gray600,
                    fontWeight: 500
                  }}
                  onMouseEnter={(e) => {
                    if (!isActive) {
                      e.currentTarget.style.backgroundColor = colors.gray50;
                      e.currentTarget.style.color = colors.gray800;
                    }
                  }}
                  onMouseLeave={(e) => {
                    if (!isActive) {
                      e.currentTarget.style.backgroundColor = 'transparent';
                      e.currentTarget.style.color = colors.gray600;
                    }
                  }}
                >
                  <Icon size={20} />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navigation;