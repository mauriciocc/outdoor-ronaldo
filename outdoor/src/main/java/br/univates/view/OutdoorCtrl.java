package br.univates.view;

import br.univates.domain.Message;
import br.univates.service.MessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/outdoor")
public class OutdoorCtrl {

    private final MessageStore messageStore;

    @Autowired
    public OutdoorCtrl(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @GetMapping("/messages")
    public List<Message> retrieveMessages() {
        return messageStore.getElements();
    }

}
