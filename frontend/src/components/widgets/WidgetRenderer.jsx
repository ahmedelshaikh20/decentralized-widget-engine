import BannerWidget from './BannerWidget';
import CardWidget from './CardWidget';
import CarouselWidget from './CarouselWidget';

import { useEffect } from 'react';
import  emitWidgetEvent  from '../../events/widgetEvents';

export default function WidgetRenderer({ widget }) {
  const map = {
    banner: BannerWidget,
    card: CardWidget,
    carousel: CarouselWidget
  };

  const Component = map[widget.componentType];

  // ✅ Hooks ALWAYS come before returns
  useEffect(() => {
    if (!Component) return;

    emitWidgetEvent({
      eventType: 'WIDGET_VIEW',
      widgetId: widget.id,
      componentType: widget.componentType
    });
  }, [Component, widget.id, widget.componentType]);

  if (!Component) {
    console.warn('Unknown widget type', widget.componentType);
    return null;
  }

  const normalizedWidget = {
    id: widget.id,
    componentType: widget.componentType,
    content: widget.payload.content,
    layout: widget.payload.layout,
    schemaVersion: widget.payload.schemaVersion
  };

  const handleClick = () => {
    emitWidgetEvent({
      eventType: 'WIDGET_CLICK',
      widgetId: widget.id,
      componentType: widget.componentType
    });
  };

  return (
    <div onClick={handleClick}>
      <Component widget={normalizedWidget} />
    </div>
  );
}
