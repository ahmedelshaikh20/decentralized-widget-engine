package org.example.widgetprocessor.model.widgetrule;

public class PersonalizationConfig {

    private String titleTemplate;
    private String subtitleTemplate;
    private String ctaLabel;
    private String ctaUrl;
    private String imageUrl;
    private String itemsTemplate;

    public PersonalizationConfig() {}


    public String getTitleTemplate() {
        return titleTemplate;
    }

    public void setTitleTemplate(String titleTemplate) {
        this.titleTemplate = titleTemplate;
    }

    public String getSubtitleTemplate() {
        return subtitleTemplate;
    }

    public void setSubtitleTemplate(String subtitleTemplate) {
        this.subtitleTemplate = subtitleTemplate;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public void setCtaLabel(String ctaLabel) {
        this.ctaLabel = ctaLabel;
    }

    public String getCtaUrl() {
        return ctaUrl;
    }

    public void setCtaUrl(String ctaUrl) {
        this.ctaUrl = ctaUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemsTemplate() {
        return itemsTemplate;
    }

    public void setItemsTemplate(String itemsTemplate) {
        this.itemsTemplate = itemsTemplate;
    }
}
