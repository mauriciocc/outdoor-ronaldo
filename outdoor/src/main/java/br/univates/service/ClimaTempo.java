package br.univates.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ClimaTempo {

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
    }

    public String find(City city) {
        HttpURLConnection conn = null;

        try {
            //Create connection
            URL url = new URL("http://www.climatempo.com.br/previsao-do-tempo/cidade/" + city.id);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "*/*");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.getResponseCode();
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
