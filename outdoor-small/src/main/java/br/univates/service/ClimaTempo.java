package br.univates.service;

import br.univates.domain.City;
import br.univates.domain.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClimaTempo {

    private final Map<City, ResultCache> cache = new ConcurrentHashMap<>();

    public Weather find(City city) {
        return cache.compute(city, (key, val) -> {
            if(val == null || (System.currentTimeMillis() - val.millis) > 300*1000){
                try {
                    URL url = new URL("http://www.climatempo.com.br/previsao-do-tempo/cidade/" + city.getId());
                    Document doc = Jsoup.parse(url, 60000);
                    return new ResultCache(city, new Weather(
                            city,
                            doc.select("#momento-temperatura").text(),
                            doc.select("#momento-condicao").text()
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else {
                return val;
            }
        }).weather;
    }

    private class ResultCache {
        final long millis = System.currentTimeMillis();
        final City city;
        final Weather weather;

        private ResultCache(City city, Weather weather) {
            this.city = city;
            this.weather = weather;
        }
    }

}
