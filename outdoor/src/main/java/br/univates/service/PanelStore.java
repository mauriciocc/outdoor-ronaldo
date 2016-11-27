package br.univates.service;

import br.univates.domain.Image;
import br.univates.domain.Panel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class PanelStore extends Store<Panel> {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private final Path images;

    public PanelStore() throws IOException {
        super(Paths.get("panels.db"));
        this.images = Paths.get("uploaded-images");
        if (Files.notExists(images)) {
            Files.createDirectory(images);
        }
    }

    private String saveImage(Image image, Panel panel) {
        String extractedBase64 = image.getContent().split("base64,")[1];
        byte[] decodedBytes = DECODER.decode(extractedBase64);

        String relativePath = panel.getId() + "." + image.getType().split("/")[1];
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
        if (!element.isContainImage() || !element.getImage().getContent().contains("base64")) {
            return super.save(element);
        }

        Image image = element.getImage();
        element.setImage(null);

        element = super.save(element);

        image.setContent(saveImage(image, element));
        element.setImage(image);

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
                .map(p -> images.resolve(p.getImage().getContent()));
    }
}
