package br.univates;

import br.univates.service.ClimaTempo;
import br.univates.service.MessageStore;
import br.univates.service.PanelStore;
import br.univates.service.TemperatureMonitor;
import br.univates.utils.Response;
import br.univates.utils.Route;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class App implements HttpHandler {


    final Path htdocs;
    final PanelStore panelStore;
    final MessageStore messageStore;
    final TemperatureMonitor temperatureMonitor;
    final ClimaTempo climaTempo;
    private final List<Route> routes;

    App(Path htdocs) {
        this.htdocs = htdocs;
        panelStore = new PanelStore();
        messageStore = new MessageStore();
        temperatureMonitor = new TemperatureMonitor();
        climaTempo = new ClimaTempo();
        AppRouteBuilder routeBuilder = new AppRouteBuilder(this);
        routes = Arrays.asList(
                routeBuilder.messagesRoute(),
                routeBuilder.panelsRoute(),
                routeBuilder.temperatureRoute(),
                routeBuilder.climaTempoRoute(),
                routeBuilder.panelImageRoute(),
                routeBuilder.removeMessageRoute(),
                routeBuilder.removePanelRoute(),
                routeBuilder.saveMessageRoute(),
                routeBuilder.savePanelRoute(),
                routeBuilder.fileHandlerRoute()
        );

    }


    @Override
    public void handle(HttpExchange t) throws IOException {
        try (OutputStream os = t.getResponseBody()) {
            handle(t, uri(t), os);
        }
    }

    private void handle(HttpExchange t, String uri, OutputStream os) {
        try {
            for (Route route : routes) {
                if (route.matches(t.getRequestMethod(), uri)) {
                    route.handle(t, uri).apply(t, os);
                    return;
                }
            }
            new Response().setStatusCode(400).setContent("NOT FOUND").apply(t, os);
        } catch (Exception e) {
            new Response().setStatusCode(500).setContent("ERROR: " + e.getMessage()).apply(t, os);
        }
    }

    private String uri(HttpExchange t) {
        String uri = t.getRequestURI().toString().substring(1);
        if (uri.isEmpty()) {
            uri = "index.html";
        }
        return uri;
    }
}
