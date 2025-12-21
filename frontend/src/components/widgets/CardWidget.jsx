import React, { useState } from 'react';
import emitWidgetEvent from "../../events/widgetEvents";

export default function CardWidget({ widget }) {
  const { content, id: widgetId, componentType } = widget;
  const [isHovered, setIsHovered] = useState(false);
  const [isButtonHovered, setIsButtonHovered] = useState(false);

  return (
    <div
      style={{
        backgroundColor: 'white',
        borderRadius: '1rem',
        boxShadow: isHovered
          ? '0 10px 15px -3px rgba(0, 0, 0, 0.1)'
          : '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        padding: '1.5rem',
        transition: 'box-shadow 0.3s, transform 0.3s',
        transform: isHovered ? 'translateY(-2px)' : 'translateY(0)'
      }}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div style={{ display: 'flex', alignItems: 'flex-start', gap: '1rem' }}>
        {content.iconUrl && (
          <div
            style={{
              width: '64px',
              height: '64px',
              backgroundColor: '#eff6ff',
              borderRadius: '0.75rem',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              flexShrink: 0,
              transition: 'transform 0.3s',
              transform: isHovered ? 'scale(1.05)' : 'scale(1)'
            }}
          >
            <img
              src={content.iconUrl}
              alt={content.title || 'Icon'}
              style={{
                width: '40px',
                height: '40px',
                transition: 'transform 0.3s',
                transform: isHovered ? 'rotate(5deg)' : 'rotate(0deg)'
              }}
            />
          </div>
        )}

        <div style={{ flex: 1 }}>
          {content.title && (
            <h3
              style={{
                fontSize: '1.25rem',
                fontWeight: 600,
                color: '#1f2937',
                margin: 0,
                marginBottom: content.subtitle ? '0.5rem' : 0
              }}
            >
              {content.title}
            </h3>
          )}

          {content.subtitle && (
            <p
              style={{
                color: '#6b7280',
                margin: 0,
                fontSize: '0.875rem',
                lineHeight: '1.5'
              }}
            >
              {content.subtitle}
            </p>
          )}
        </div>
      </div>

      {content.cta && (
        <button
          onClick={() => {
            // ✅ Emit widget click event
            emitWidgetEvent({
              widgetId,
              widgetType: componentType,
              actionType: "CLICK",
              metadata: {
                ctaUrl: content.cta.url,
                label: content.cta.label
              }
            });

            console.log("Navigate to:", content.cta.url);
          }}
          onMouseEnter={(e) => {
            setIsButtonHovered(true);
            e.currentTarget.style.backgroundColor = '#1d4ed8';
            e.currentTarget.style.transform = 'translateY(-2px)';
            e.currentTarget.style.boxShadow =
              '0 6px 8px -1px rgba(0, 0, 0, 0.15)';
          }}
          onMouseLeave={(e) => {
            setIsButtonHovered(false);
            e.currentTarget.style.backgroundColor = '#2563eb';
            e.currentTarget.style.transform = 'translateY(0)';
            e.currentTarget.style.boxShadow =
              '0 4px 6px -1px rgba(0, 0, 0, 0.1)';
          }}
          style={{
            backgroundColor: '#2563eb',
            color: 'white',
            padding: '0.75rem 1.5rem',
            borderRadius: '0.5rem',
            fontWeight: 500,
            width: '100%',
            border: 'none',
            cursor: 'pointer',
            transition: 'all 0.2s',
            marginTop: '1rem',
            boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '0.5rem'
          }}
        >
          <span>{content.cta.label}</span>
          <span
            style={{
              transition: 'transform 0.2s',
              transform: isButtonHovered ? 'translateX(4px)' : 'translateX(0)',
              display: 'inline-block'
            }}
          >
            →
          </span>
        </button>
      )}
    </div>
  );
}
