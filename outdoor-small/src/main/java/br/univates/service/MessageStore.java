package br.univates.service;

import br.univates.domain.Message;

import java.nio.file.Paths;

public class MessageStore extends Store<Message> {
    public MessageStore() {
        super(Paths.get("messages.db"));
    }
}
