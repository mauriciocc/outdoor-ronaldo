package br.univates;

import br.univates.domain.*;
import br.univates.service.ClimaTempo;
import br.univates.service.MessageStore;
import br.univates.service.PanelStore;
import br.univates.service.TemperatureMonitor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 1024);
        Path htdocs = Paths.get("static");
        LOG.info("Serving files from: " + htdocs.toAbsolutePath());
        server.createContext("/", new App(htdocs));
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    static class App implements HttpHandler {

        public static final String API_MANAGE = "api/manage/";
        public static final String GET = "GET";
        public static final String POST = "POST";
        private final Path htdocs;

        private final PanelStore panelStore;
        private final MessageStore messageStore;
        private final TemperatureMonitor temperatureMonitor;
        private final ClimaTempo climaTempo;
        private final Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Number.class, new IntegerTypeAdapter()).create();

        public App(Path htdocs) {
            this.htdocs = htdocs;
            panelStore = new PanelStore();
            messageStore = new MessageStore();
            temperatureMonitor = new TemperatureMonitor();
            climaTempo = new ClimaTempo();
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String uri = uri(t);
            OutputStream os = t.getResponseBody();

            if (uri.startsWith(API_MANAGE)) {
                String[] split = uri.substring(API_MANAGE.length()).split("\\?");
                uri = split[0];
                String params = split.length > 1 ? split[1] : "";
                String method = t.getRequestMethod();
                if (method.equalsIgnoreCase(GET)) {
                    t.getResponseHeaders().add("Content-Type", "application/json");
                    switch (uri) {
                        case "messages": {
                            byte[] bytes = gson.toJson(
                                    messageStore.getElements()
                                            .stream()
                                            .sorted(RoutedElement.ByOrder)
                                            .collect(Collectors.toList())
                            ).getBytes(UTF_8);
                            t.sendResponseHeaders(200, bytes.length);
                            os.write(bytes);
                            break;
                        }
                        case "panels": {
                            byte[] bytes = gson.toJson(
                                    panelStore.getElements()
                                            .stream()
                                            .sorted(RoutedElement.ByOrder)
                                            .collect(Collectors.toList())
                            ).getBytes(UTF_8);
                            t.sendResponseHeaders(200, bytes.length);
                            os.write(bytes);
                            break;
                        }
                        case "temperature": {
                            byte[] bytes = String.valueOf(temperatureMonitor.readPort(TemperatureMonitor.AIN1)).getBytes(UTF_8);
                            t.sendResponseHeaders(200, bytes.length);
                            os.write(bytes);
                            break;
                        }
                        case "clima-tempo": {
                            City city = City.valueOf(params.split("city=")[1]);
                            Weather weather = climaTempo.find(city);
                            byte[] bytes = gson.toJson(weather).getBytes(UTF_8);
                            t.sendResponseHeaders(200, bytes.length);
                            os.write(bytes);
                            break;
                        }
                    }
                    String[] splitedUri = uri.split("/");

                    if("panels".equals(splitedUri[0])) {
                        int id = Integer.parseInt(splitedUri[1]);
                        panelStore.imageRef(id).ifPresent(file -> {
                            t.getResponseHeaders().add("Content-Type", "image");
                            try {
                                t.sendResponseHeaders(200, Files.size(file));
                                Files.copy(file, os);
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        });

                    }
                }
                if (method.equalsIgnoreCase(POST)) {
                    switch (uri) {
                        case "messages": {
                            try(InputStreamReader reader = new InputStreamReader(t.getRequestBody(), UTF_8)) {
                                Message message = gson.fromJson(reader, Message.class);
                                messageStore.save(message);
                                t.sendResponseHeaders(200, 0);
                                break;
                            }
                        }
                        case "panels": {
                            try(InputStreamReader reader = new InputStreamReader(t.getRequestBody(), UTF_8)) {
                                Panel panel = gson.fromJson(reader, Panel.class);
                                panelStore.save(panel);
                                t.sendResponseHeaders(200, 0);
                                break;
                            }
                        }
                    }
                }
            } else {

                try {
                    Path file = htdocs.resolve(uri);
                    if (Files.exists(file)) {
                        String type = Files.probeContentType(file);
                        if (type == null) {
                            if (uri.contains("woff") || uri.contains("ttf")) {
                                type = "application/font-" + uri.substring(uri.lastIndexOf('.'));
                            }
                        }
                        t.getResponseHeaders().add(
                                "Content-Type",
                                type
                        );
                        byte[] content = Files.readAllBytes(file);
                        t.sendResponseHeaders(200, content.length);
                        os.write(content);
                    } else {
                        byte[] bytes = "Not Found".getBytes();
                        t.sendResponseHeaders(404, bytes.length);
                        os.write(bytes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            os.close();
        }

        private String uri(HttpExchange t) {
            String uri = t.getRequestURI().toString().substring(1);
            if (uri.isEmpty()) {
                uri = "index.html";
            }
            return uri;
        }
    }

}
