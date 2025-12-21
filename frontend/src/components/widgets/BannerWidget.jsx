import React, { useState } from "react";
import emitWidgetEvent from "../../events/widgetEvents";

export default function BannerWidget({ widget }) {
  const { content, id: widgetId, componentType } = widget;
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div
      style={{
        position: "relative",
        borderRadius: "1rem",
        overflow: "hidden",
        boxShadow: isHovered
          ? "0 20px 25px -5px rgba(0, 0, 0, 0.15)"
          : "0 10px 15px -3px rgba(0, 0, 0, 0.1)",
        height: "256px",
        transition: "box-shadow 0.3s, transform 0.3s",
        transform: isHovered ? "scale(1.01)" : "scale(1)",
        cursor: content.cta?.url ? "pointer" : "default"
      }}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      {content.imageUrl && (
        <img
          src={content.imageUrl}
          alt={content.title || "Banner image"}
          style={{
            width: "100%",
            height: "100%",
            objectFit: "cover",
            transition: "transform 0.3s",
            transform: isHovered ? "scale(1.05)" : "scale(1)"
          }}
        />
      )}

      <div
        style={{
          position: "absolute",
          inset: 0,
          background:
            "linear-gradient(to top, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.3) 50%, transparent 100%)"
        }}
      />

      <div
        style={{
          position: "absolute",
          inset: 0,
          padding: "1.5rem",
          display: "flex",
          flexDirection: "column",
          justifyContent: "flex-end"
        }}
      >
        {content.title && (
          <h2
            style={{
              fontSize: "1.875rem",
              fontWeight: "bold",
              color: "white",
              margin: 0,
              textShadow: "0 2px 4px rgba(0,0,0,0.3)"
            }}
          >
            {content.title}
          </h2>
        )}

        {content.subtitle && (
          <p
            style={{
              color: "rgba(255,255,255,0.95)",
              fontSize: "1.125rem",
              margin: "0.5rem 0 1rem 0",
              textShadow: "0 1px 2px rgba(0,0,0,0.3)"
            }}
          >
            {content.subtitle}
          </p>
        )}

        {content.cta?.url && (
          <button
            onClick={(e) => {
              e.stopPropagation();

              emitWidgetEvent({
                widgetId,
                widgetType: componentType, // banner
                actionType: "CLICK",
                metadata: {
                  ctaUrl: content.cta.url,
                  label: content.cta.label
                }
              });

              console.log("Navigate to:", content.cta.url);
            }}
            style={{
              backgroundColor: "#2563eb",
              color: "white",
              padding: "0.75rem 1.5rem",
              borderRadius: "0.5rem",
              fontWeight: 500,
              width: "fit-content",
              border: "none",
              cursor: "pointer",
              transition: "background-color 0.2s, transform 0.2s",
              boxShadow: "0 4px 6px -1px rgba(0, 0, 0, 0.1)"
            }}
          >
            {content.cta.label}
          </button>
        )}
      </div>
    </div>
  );
}
