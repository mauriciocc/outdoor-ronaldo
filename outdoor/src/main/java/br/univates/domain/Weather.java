package br.univates.domain;

public class Weather {

    private final City city;
    private final String cityLabel;
    private final String temperature;
    private final String description;

    public Weather(City city, String temperature, String description) {
        this.city = city;
        this.cityLabel = city.getLabel();
        this.temperature = temperature;
        this.description = description;
    }

    public City getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getCityLabel() {
        return cityLabel;
    }

}
