package br.univates.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;

public abstract class Json {

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Number.class, new IntegerTypeAdapter())
            .create();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

}
