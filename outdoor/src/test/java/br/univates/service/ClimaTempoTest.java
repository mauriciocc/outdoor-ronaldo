package br.univates.service;

import br.univates.domain.City;
import br.univates.domain.Weather;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ClimaTempoTest {

    private final ClimaTempo ct = new ClimaTempo();

    @Test
    public void find() throws Exception {
        Weather result = ct.find(City.Lajeado);
        System.out.println(result);
    }

}