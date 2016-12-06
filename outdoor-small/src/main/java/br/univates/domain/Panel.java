package br.univates.domain;


public class Panel extends RoutedElement {

    private PanelType type = PanelType.Regular;
    private String title = "SEM TITULO";
    private Integer titleFontSize = 18;
    private String titleFontColor = "#333";
    private String imageContent = "";
    private String imageType = "";
    private String message = "SEM MENSAGEM";
    private Integer messageFontSize = 14;
    private String messageFontColor = "#333";
    private String bgColor = "#FFF";

    public Panel() {
    }

    public PanelType getType() {
        return type;
    }

    public void setType(PanelType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public boolean isContainImage() {
        return !imageContent.isEmpty();
    }

    public Integer getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(Integer titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public String getTitleFontColor() {
        return titleFontColor;
    }

    public void setTitleFontColor(String titleFontColor) {
        this.titleFontColor = titleFontColor;
    }

    public Integer getMessageFontSize() {
        return messageFontSize;
    }

    public void setMessageFontSize(Integer messageFontSize) {
        this.messageFontSize = messageFontSize;
    }

    public String getMessageFontColor() {
        return messageFontColor;
    }

    public void setMessageFontColor(String messageFontColor) {
        this.messageFontColor = messageFontColor;
    }
}

