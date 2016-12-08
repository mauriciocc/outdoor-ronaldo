package br.univates.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Route {

    public static final String QUERY = "\\?";
    public static final String PATH = "/";
    public static final String KEY_VAL = "=";
    public static final String CATCH_ALL = "*";
    public static final String REQUEST_URI = "REQUEST_URI";

    private final String method;
    private final String template;
    private final Handler handler;

    public Route(Method method, String template, Handler handler) {
        this.method = method.toString();
        this.template = template;
        this.handler = handler;
    }

    public Response handle(HttpExchange t, String uri) {
        try (InputStreamReader body = new InputStreamReader(t.getRequestBody(), UTF_8)) {
            return handler.handle(body, pathParams(uri), queryParams(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> pathParams(final String uri) {
        Map<String, String> params = new LinkedHashMap<>();
        String[] uriParts = uri.split(PATH);
        String[] parts = template.split(PATH);
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("{")) {
                String key = part.substring(1, part.length() - 1);
                params.put(key, uriParts[i]);
            }
        }
        params.put(REQUEST_URI, uri);
        return params;
    }

    public Map<String, String> queryParams(final String uri) {
        String[] split = uri.split(QUERY);
        if (split.length > 1) {
            String[] params = split[1].split(KEY_VAL);
            Map<String, String> paramMap = new LinkedHashMap<>();
            for (int i = 0; i < params.length; i += 2) {
                paramMap.put(params[i], params[i + 1]);
            }
            return paramMap;
        } else {
            return Collections.emptyMap();
        }
    }

    public boolean matches(final String method, final String uri) {
        if (template.equals(CATCH_ALL)) {
            return true;
        }

        if (!this.method.equalsIgnoreCase(method)) {
            return false;
        }

        String[] parts = template.split(PATH);
        String[] uriParts = uri.split(QUERY)[0].split(PATH);

        if (uriParts.length != parts.length) {
            return false;
        }

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("{")) {
                continue;
            }
            if (!part.equals(uriParts[i])) {
                return false;
            }
        }

        return true;
    }

    public static interface Handler {
        Response handle(Reader body, Map<String, String> path, Map<String, String> query);
    }

    public static enum Method {
        GET,
        POST,
        DELETE
    }

}
