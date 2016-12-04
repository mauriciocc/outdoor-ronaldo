package br.univates.domain;

public class Message extends RoutedElement {

    private MessageType type;
    private String content;

    public Message() {
    }

    public Message(int timeOnScreen, String content) {
        this.setTimeOnScreen(timeOnScreen);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

}