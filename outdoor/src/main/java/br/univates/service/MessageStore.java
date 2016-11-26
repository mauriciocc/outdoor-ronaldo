package br.univates.service;

import br.univates.domain.Message;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class MessageStore extends Store<Message> {
    public MessageStore() {
        super(Paths.get("messages.db"));
    }
}
