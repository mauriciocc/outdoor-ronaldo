package br.univates.service;

import br.univates.domain.Panel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public class PanelStore extends Store<Panel> {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private final Path images;

    public PanelStore() {
        super(Paths.get("panels.db"));
        this.images = Paths.get("uploaded-images");
        if (Files.notExists(images)) {
            try {
                Files.createDirectory(images);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String saveImage(String image, Panel panel) {
        String extractedBase64 = image.split("base64,")[1];
        byte[] decodedBytes = DECODER.decode(extractedBase64);

        String relativePath = panel.getId() + "." + panel.getImageType().split("/")[1];
        Path imageFile = images.resolve(relativePath);

        try {
            Files.deleteIfExists(imageFile);
            Files.write(imageFile, decodedBytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return relativePath;
    }

    @Override
    public Panel save(Panel element) {
        if (!element.isContainImage() || !element.getImageContent().contains("base64")) {
            return super.save(element);
        }

        String image = element.getImageContent();
        element.setImageContent(null);

        element = super.save(element);

        element.setImageContent(saveImage(image, element));

        return super.save(element);
    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        imageRef(id).ifPresent(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Optional<Path> imageRef(Integer id) {
        return getElements().stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findAny()
                .map(p -> images.resolve(p.getImageContent()));
    }
}
