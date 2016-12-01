package br.univates.domain;

public enum City {
    Lajeado(1399),
    Gramado(780),
    POA(363),
    Guapore(2011),
    Torres(370);

    private final int id;

    City(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
