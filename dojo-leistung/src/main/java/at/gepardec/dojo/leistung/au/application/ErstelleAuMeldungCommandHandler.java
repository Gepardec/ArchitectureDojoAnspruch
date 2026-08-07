package at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.leistung.au.application.port.AuMeldungRepository;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungCommand;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungUseCase;
import at.gepardec.dojo.leistung.au.domain.AuMeldung;

import java.util.Objects;

public class ErstelleAuMeldungCommandHandler implements ErstelleAuMeldungUseCase {
    private final AuMeldungRepository repository;

    public ErstelleAuMeldungCommandHandler(AuMeldungRepository repository) {
        this.repository = repository;
    }

    @Override
    public void erstelleAuMeldung(ErstelleAuMeldungCommand command) {
        Objects.requireNonNull(command);
        AuMeldung meldung = new AuMeldung(command.svnr(), command.auBeginn());
        repository.speichere(meldung);
    }
}
