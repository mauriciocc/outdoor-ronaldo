package br.univates.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Response {

    public static final byte[] EMPTY = new byte[0];
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    private Map<String, String> headers = new HashMap<>();
    private int statusCode = 200;
    private byte[] content = EMPTY;

    public Response() {
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getContent() {
        return content;
    }

    public Response setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Response setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Response setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public Response setContent(String content) {
        setContent(content.getBytes(StandardCharsets.UTF_8));
        return this;
    }

    public Response put(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Response asJson() {
        return put(CONTENT_TYPE, APPLICATION_JSON);
    }

    public Response apply(HttpExchange t, OutputStream os) {
        try {
            Headers rheaders = t.getResponseHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                rheaders.add(entry.getKey(), entry.getValue());
            }
            t.sendResponseHeaders(statusCode, content.length);
            if(content.length > 0) {
                os.write(content);
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response asImage(Path file) {
        String format = file.toString().split("\\.")[1];
        return put(CONTENT_TYPE, "image/"+ format);
    }

    public Response ofType(String type) {
        return put(CONTENT_TYPE, type);
    }

}
