package br.univates.view;

import br.univates.domain.Message;
import br.univates.domain.Panel;
import br.univates.service.MessageStore;
import br.univates.service.PanelStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manage")
public class ManageCtrl {

    private final MessageStore messageStore;
    private final PanelStore panelStore;

    @Autowired
    public ManageCtrl(MessageStore messageStore, PanelStore panelStore) {
        this.messageStore = messageStore;
        this.panelStore = panelStore;
    }

    @GetMapping("/messages")
    public List<Message> findMessages() {
        return messageStore.getElements();
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
        return panelStore.getElements();
    }

    @PostMapping("/panels")
    public Panel saveMessage(Panel panel) {
        return panelStore.save(panel);
    }

    @DeleteMapping("/panels/{id}")
    public void panels(@PathVariable Integer id) {
        panelStore.remove(id);
    }

}