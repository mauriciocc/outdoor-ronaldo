package br.univates.service;

import br.univates.domain.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Store<T extends Model> {

    private final Path store;
    private final AtomicInteger idGenerator;
    private final List<T> elements;

    public Store(Path store) {
        this.store = store;
        if (Files.exists(store)) {
            this.elements = readFile();
            this.idGenerator = new AtomicInteger(
                    this.getElements().stream().map(Model::getId).max(Integer::compare).orElse(0)
            );
        } else {
            this.idGenerator = new AtomicInteger(0);
            this.elements = Collections.synchronizedList(new ArrayList<>());
        }
    }

    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public T save(T element) {
        if (!element.isPersisted()) {
            element.setId(idGenerator.incrementAndGet());
            elements.add(element);
        } else {
            elements.set(elements.indexOf(element), element);
        }
        writeFile();
        return element;
    }

    public void remove(Integer id) {
        elements.removeIf(message -> Objects.equals(id, message.getId()));
        writeFile();
    }

    private void writeFile() {
        try (OutputStream os = Files.newOutputStream(store);
             ObjectOutputStream objOs = new ObjectOutputStream(os)) {
            objOs.writeObject(elements);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> readFile() {
        try (InputStream is = Files.newInputStream(store);
             ObjectInputStream objIs = new ObjectInputStream(is)) {
            return (List<T>) objIs.readObject();
        } catch (Exception e) {
            try {
                Files.deleteIfExists(store);
            } catch (IOException e1) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>();
    }
}
