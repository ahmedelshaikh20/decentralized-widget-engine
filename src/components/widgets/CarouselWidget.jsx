import React, { useState } from 'react';
import emitWidgetEvent from '../../events/widgetEvents';

function CarouselCard({ item, widgetId, widgetType }) {
  const [isHovered, setIsHovered] = useState(false);

  const cta = item.cta ?? (
    item.ctaUrl
      ? { url: item.ctaUrl, label: item.ctaLabel }
      : null
  );

  return (
    <div
      style={{
        flexShrink: 0,
        width: '256px',
        backgroundColor: 'white',
        borderRadius: '1rem',
        overflow: 'hidden',
        cursor: cta ? 'pointer' : 'default',
        boxShadow: isHovered
          ? '0 20px 25px -5px rgba(0, 0, 0, 0.2)'
          : '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        transition: 'all 0.3s',
        transform: isHovered
          ? 'translateY(-4px) scale(1.02)'
          : 'translateY(0) scale(1)'
      }}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={() => {
        if (!cta) return;

        // ✅ emit widget item click
        emitWidgetEvent({
          widgetId,
          widgetType,
          actionType: 'CLICK',
          metadata: {
            ctaUrl: cta.url,
            label: cta.label,
            itemTitle: item.title
          }
        });

        console.log('Navigate to:', cta.url);
      }}
    >
      {item.imageUrl && (
        <div style={{ position: 'relative', height: '160px', overflow: 'hidden' }}>
          <img
            src={item.imageUrl}
            alt={item.title || 'Carousel item'}
            style={{
              width: '100%',
              height: '100%',
              objectFit: 'cover',
              transition: 'transform 0.3s',
              transform: isHovered ? 'scale(1.1)' : 'scale(1)'
            }}
          />

          <div
            style={{
              position: 'absolute',
              inset: 0,
              background:
                'linear-gradient(to top, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.2) 50%, transparent 100%)'
            }}
          />

          <div
            style={{
              position: 'absolute',
              bottom: '.75rem',
              left: '.75rem',
              right: '.75rem'
            }}
          >
            <p
              style={{
                fontWeight: 600,
                fontSize: '.875rem',
                color: 'white',
                margin: 0
              }}
            >
              {item.title}
            </p>

            {cta && (
              <p
                style={{
                  fontSize: '.75rem',
                  marginTop: '.25rem',
                  color: 'rgba(255,255,255,0.95)',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '0.25rem'
                }}
              >
                {cta.label}
                <span
                  style={{
                    transition: 'transform 0.2s',
                    transform: isHovered ? 'translateX(4px)' : 'translateX(0)'
                  }}
                >
                  →
                </span>
              </p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default function CarouselWidget({ widget }) {
  const { content, id: widgetId, componentType } = widget;
  const [isButtonHovered, setIsButtonHovered] = useState(false);

  const items = Array.isArray(content.items) ? content.items : [];
  if (!items.length) return null;

  return (
    <div
      style={{
        background: 'linear-gradient(135deg, #2563eb 0%, #1e40af 100%)',
        borderRadius: '1rem',
        padding: '1.5rem'
      }}
    >
      {content.title && <h2 style={{ color: 'white' }}>{content.title}</h2>}
      {content.subtitle && <p style={{ color: 'white' }}>{content.subtitle}</p>}

      <div
        style={{
          display: 'flex',
          gap: '1rem',
          overflowX: 'auto',
          paddingBottom: '.5rem'
        }}
      >
        {items.map((item, index) => (
          <CarouselCard
            key={index}
            item={item}
            widgetId={widgetId}          
            widgetType={componentType}  
          />
        ))}
      </div>

      {content.cta && (
        <button
          onClick={() => {
            emitWidgetEvent({
              widgetId,
              widgetType: componentType,
              actionType: 'CLICK',
              metadata: {
                ctaUrl: content.cta.url,
                label: content.cta.label
              }
            });

            console.log('Navigate to:', content.cta.url);
          }}
          onMouseEnter={(e) => {
            setIsButtonHovered(true);
            e.currentTarget.style.backgroundColor = '#eff6ff';
            e.currentTarget.style.transform = 'translateY(-2px)';
            e.currentTarget.style.boxShadow =
              '0 6px 8px -1px rgba(0, 0, 0, 0.2)';
          }}
          onMouseLeave={(e) => {
            setIsButtonHovered(false);
            e.currentTarget.style.backgroundColor = 'white';
            e.currentTarget.style.transform = 'translateY(0)';
            e.currentTarget.style.boxShadow =
              '0 4px 6px -1px rgba(0, 0, 0, 0.1)';
          }}
          style={{
            backgroundColor: 'white',
            color: '#2563eb',
            padding: '0.75rem 1.5rem',
            borderRadius: '0.5rem',
            fontWeight: 600,
            border: 'none',
            cursor: 'pointer',
            transition: 'all 0.2s',
            marginTop: '1.5rem',
            display: 'inline-flex',
            alignItems: 'center',
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
