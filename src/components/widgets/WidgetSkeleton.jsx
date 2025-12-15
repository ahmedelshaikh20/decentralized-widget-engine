import Skeleton from "../common/Skeleton";

export default function WidgetSkeleton() {
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "1rem",
        padding: "1rem",
        background: "#fff",
        borderRadius: "1rem"
      }}
    >
      {/* Title */}
      <Skeleton width="60%" height="24px" />

      {/* Subtitle */}
      <Skeleton width="40%" height="18px" />

      {/* CTA Button */}
      <div style={{ marginTop: "0.5rem" }}>
        <Skeleton width="30%" height="36px" />
      </div>
    </div>
  );
}
