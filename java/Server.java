import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class Server {
    

    public static void main(String[] args) throws Exception {                
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 128);
        Path htdocs = Paths.get("");
        server.createContext("/", new App(htdocs));
        server.setExecutor(Executors.newFixedThreadPool(32));
        server.start();
    }

    static class App implements HttpHandler {
        
        private final Path htdocs;

        public App(Path htdocs) {
            this.htdocs = htdocs;
        }
                
        @Override
        public void handle(HttpExchange t) throws IOException {            
            final String uri = uri(t);
            OutputStream os = t.getResponseBody();
            try {
                Path file = htdocs.resolve(uri);                
                if(Files.exists(file)) {         
                    System.out.println(uri + " " + Files.probeContentType(file));
                    
                    t.getResponseHeaders().add(
                            "Content-Type", 
                            Files.probeContentType(file)
                    );                    
                    byte[] content = Files.readAllBytes(file);
                    t.sendResponseHeaders(200, content.length);
                    os.write(content);                    
                } else {
                    byte[] bytes = "Not Found".getBytes();
                    t.sendResponseHeaders(404, bytes.length);
                    os.write(bytes);
                }                                
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                os.close();                
            }
        }
        
        private String uri(HttpExchange t) {
            String uri = t.getRequestURI().toString().substring(1);                       
            if(uri.isEmpty()) {
                uri = "index.html";            
            }
            return uri;
        }
    }

}
