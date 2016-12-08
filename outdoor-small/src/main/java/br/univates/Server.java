package br.univates;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 1024);

        Path htdocs = Paths.get("static");
        LOG.info("Serving files from: " + htdocs.toAbsolutePath());

        server.createContext("/", new App(htdocs));

        int totalThreads = Runtime.getRuntime().availableProcessors() * 2 + 1;
        server.setExecutor(Executors.newFixedThreadPool(totalThreads));

        server.start();
    }

}
