package br.univates.view;

import br.univates.domain.*;
import br.univates.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manage")
public class ManageCtrl {

    private final MessageStore messageStore;
    private final PanelStore panelStore;
    private final TemperatureMonitor temperatureMonitor;
    private final ClimaTempo climaTempo;

    @Autowired
    public ManageCtrl(MessageStore messageStore,
                      PanelStore panelStore,
                      TemperatureMonitor temperatureMonitor,
                      ClimaTempo climaTempo) {
        this.messageStore = messageStore;
        this.panelStore = panelStore;
        this.temperatureMonitor = temperatureMonitor;
        this.climaTempo = climaTempo;
    }

    @GetMapping("/messages")
    public List<Message> findMessages() {
        return messageStore.getElements()
                .stream()
                .sorted(RoutedElement.ByOrder)
                .collect(Collectors.toList());
    }

    @PostMapping("/messages")
    public Message saveMessage(Message message) {
        return messageStore.save(message);
    }

    @DeleteMapping("/messages/{id}")
    public void saveMessage(@PathVariable Integer id) {
        messageStore.remove(id);
    }

    @GetMapping("/panels")
    public List<Panel> findPanels() {
        return panelStore.getElements()
                .stream()
                .sorted(RoutedElement.ByOrder)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/panels/{id}/image", produces = "image/*")
    public ResponseEntity<FileSystemResource> findPanels(@PathVariable Integer id) {
        return panelStore.imageRef(id)
                .map(p -> ResponseEntity.ok(new FileSystemResource(p.toFile())))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/panels")
    public Panel saveMessage(Panel panel) {
        return panelStore.save(panel);
    }

    @DeleteMapping("/panels/{id}")
    public void panels(@PathVariable Integer id) {
        panelStore.remove(id);
    }

    @GetMapping("/temperature")
    public int findTemperature() {
        return temperatureMonitor.readPort(TemperatureMonitor.AIN1);
    }

    @GetMapping(value = "/clima-tempo")
    public Weather findOnClimaTempo(@RequestParam City city) {
        return climaTempo.find(city);
    }

}