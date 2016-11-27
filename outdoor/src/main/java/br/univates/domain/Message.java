package br.univates.domain;

public class Message extends RoutedElement {

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
}
