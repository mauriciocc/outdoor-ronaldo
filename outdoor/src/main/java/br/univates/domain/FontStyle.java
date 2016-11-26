package br.univates.domain;

import java.io.Serializable;

public class FontStyle implements Serializable {

    private int size;
    private String color;

    public FontStyle() {
    }

    public FontStyle(int size, String color) {
        this.size = size;
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
