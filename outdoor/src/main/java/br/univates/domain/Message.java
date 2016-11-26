package br.univates.domain;

public class Message extends Model {

    private int timeOnScreen;
    private String content;

    public Message() {
    }

    public Message(int timeOnScreen, String content) {
        this.timeOnScreen = timeOnScreen;
        this.content = content;
    }

    public int getTimeOnScreen() {
        return timeOnScreen;
    }

    public void setTimeOnScreen(int timeOnScreen) {
        this.timeOnScreen = timeOnScreen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
