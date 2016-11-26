package br.univates.service;

import br.univates.domain.Message;
import br.univates.domain.Panel;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class PanelStore extends Store<Panel> {
    public PanelStore() {
        super(Paths.get("panels.db"));
    }
}
