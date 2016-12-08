package br.univates;

import br.univates.domain.*;
import br.univates.service.TemperatureMonitor;
import br.univates.utils.Json;
import br.univates.utils.Response;
import br.univates.utils.Route;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static br.univates.utils.Json.toJson;

class AppRouteBuilder {

    private static final String API_MANAGE = "api/manage";

    private final App app;

    AppRouteBuilder(App app) {
        this.app = app;
    }

    Route panelsRoute() {
        return new Route(Route.Method.GET, API_MANAGE + "/panels",
                (body, path, query) -> {
                    List<Panel> panels = app.panelStore.getElements()
                            .stream()
                            .sorted(RoutedElement.ByOrder)
                            .collect(Collectors.toList());
                    return new Response()
                            .setContent(toJson(panels))
                            .asJson();
                });
    }

    Route messagesRoute() {
        return new Route(Route.Method.GET, API_MANAGE + "/messages",
                (body, path, query) -> {
                    List<Message> messages = app.messageStore.getElements()
                            .stream()
                            .sorted(RoutedElement.ByOrder)
                            .collect(Collectors.toList());
                    return new Response()
                            .setContent(toJson(messages))
                            .asJson();
                });
    }

    Route temperatureRoute() {
        return new Route(Route.Method.GET, API_MANAGE + "/temperature",
                (body, path, query) -> {
                    String temperature = String.valueOf(app.temperatureMonitor.readPort(TemperatureMonitor.AIN1));
                    return new Response()
                            .setContent(temperature)
                            .asJson();
                });
    }

    Route climaTempoRoute() {
        return new Route(Route.Method.GET, API_MANAGE + "/clima-tempo",
                (body, path, query) -> {
                    City city = City.valueOf(query.get("city"));
                    Weather weather = app.climaTempo.find(city);
                    return new Response()
                            .setContent(toJson(weather))
                            .asJson();
                });
    }

    Route panelImageRoute() {
        return new Route(Route.Method.GET, API_MANAGE + "/panels/{id}/image",
                (body, path, query) -> {
                    int id = Integer.parseInt(path.getOrDefault("id", "0"));
                    return app.panelStore.imageRef(id).map(file -> {
                        try {
                            byte[] image = Files.readAllBytes(file);
                            return new Response()
                                    .setContent(image)
                                    .asImage(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).orElseGet(() -> new Response().setStatusCode(404));
                });
    }

    Route removePanelRoute() {
        return new Route(Route.Method.DELETE, API_MANAGE + "/panels/{id}",
                (body, path, query) -> {
                    int id = Integer.parseInt(path.getOrDefault("id", "0"));
                    app.panelStore.remove(id);
                    return new Response();
                });
    }

    Route removeMessageRoute() {
        return new Route(Route.Method.DELETE, API_MANAGE + "/messages/{id}",
                (body, path, query) -> {
                    int id = Integer.parseInt(path.getOrDefault("id", "0"));
                    app.messageStore.remove(id);
                    return new Response();
                });
    }

    Route saveMessageRoute() {
        return new Route(Route.Method.POST, API_MANAGE + "/messages",
                (body, path, query) -> {
                    final Message message = Json.fromJson(body, Message.class);
                    app.messageStore.save(message);
                    return new Response();
                });
    }

    Route savePanelRoute() {
        return new Route(Route.Method.POST, API_MANAGE + "/panels",
                (body, path, query) -> {
                    final Panel panel = Json.fromJson(body, Panel.class);
                    app.panelStore.save(panel);
                    return new Response();
                });
    }

    Route fileHandlerRoute() {
        return new Route(Route.Method.POST, Route.CATCH_ALL,
                (body, path, query) -> {
                    String uri = path.get(Route.REQUEST_URI);
                    try {

                        Path file = app.htdocs.resolve(uri);
                        if (Files.exists(file)) {
                            String type = Files.probeContentType(file);
                            if (type == null) {
                                if (uri.contains("woff") || uri.contains("ttf")) {
                                    type = "application/font-" + uri.substring(uri.lastIndexOf('.'));
                                }
                            }
                            byte[] content = Files.readAllBytes(file);
                            return new Response().setContent(content).ofType(type);
                        } else {
                            return new Response().setStatusCode(404);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
