package br.univates.domain;

public enum City {
    Lajeado(1399, "Lajeado"),
    Gramado(780, "Gramado"),
    POA(363, "Porto Alegre"),
    Guapore(2011, "Guaporé"),
    Torres(370, "Torres");

    private final int id;
    private final String label;

    City(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
