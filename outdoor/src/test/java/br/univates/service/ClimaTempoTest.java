package br.univates.service;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ClimaTempoTest {

    private final ClimaTempo ct = new ClimaTempo();

    @Test
    public void find() throws Exception {
        String result = ct.find(ClimaTempo.City.Lajeado);
        System.out.println(result);
    }

}