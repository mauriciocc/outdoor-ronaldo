package br.univates.domain;


public class Panel extends RoutedElement {

    private PanelType type = PanelType.Regular;
    private String title = "SEM TITULO";
    private FontStyle titleStyle = new FontStyle(18, "#333");
    private Image image = new Image();
    private String message = "SEM MENSAGEM";
    private FontStyle messageStyle = new FontStyle(18, "#333");
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

    public FontStyle getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(FontStyle titleStyle) {
        this.titleStyle = titleStyle;
    }

    public Image getImage() {
        if (image == null) {
            image = new Image();
        }
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FontStyle getMessageStyle() {
        return messageStyle;
    }

    public void setMessageStyle(FontStyle messageStyle) {
        this.messageStyle = messageStyle;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isContainImage() {
        return !getImage().getContent().isEmpty();
    }
}
