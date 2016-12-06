package br.univates.domain;

import java.io.Serializable;

public class FontStyle implements Serializable {

    private Integer size;
    private String color;

    public FontStyle() {
    }

    public FontStyle(Integer size, String color) {
        this.size = size;
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
