package br.univates.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Route {

    private final String method;
    private final String template;
    private final Handler handler;

    public Route(String method, String template, Handler handler) {
        this.method = method;
        this.template = template;
        this.handler = handler;
    }

    public void handle(HttpExchange t, String uri, OutputStream os) {
        handler.handle(t, uri, os);
    }

    public Map<String, String> pathParams(final String uri) {
        Map<String, String> params = new LinkedHashMap<>();
        String[] uriParts = uri.split("/");
        String[] parts = template.split("/");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("{")) {
                String key = part.substring(1, part.length() - 1);
                params.put(key, uriParts[i]);
            }
        }
        return params;
    }

    public Map<String, String> queryParams(final String uri) {
        String[] split = uri.split("\\?");
        if (split.length > 1) {
            String[] params = split[1].split("=");
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
        if (!this.method.equalsIgnoreCase(method)) {
            return false;
        }

        String[] parts = template.split("/");
        String[] uriParts = uri.split("/");

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
        void handle(HttpExchange t, String uri, OutputStream os);
    }
}
