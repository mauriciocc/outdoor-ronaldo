package br.univates.service;

import br.univates.domain.City;
import br.univates.domain.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class ClimaTempo {

    public Weather find(City city) {
        try {
            URL url = new URL("http://www.climatempo.com.br/previsao-do-tempo/cidade/" + city.getId());
            Document doc = Jsoup.parse(url, 60000);
            return new Weather(
                    city,
                    doc.select("#momento-temperatura").text(),
                    doc.select("#momento-condicao").text()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
