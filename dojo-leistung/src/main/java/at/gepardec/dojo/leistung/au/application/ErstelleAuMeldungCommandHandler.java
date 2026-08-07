package at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.leistung.au.application.port.AuMeldungRepository;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungCommand;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungUseCase;
import at.gepardec.dojo.leistung.au.application.port.LeistungsanspruchPruefungPort;
import at.gepardec.dojo.leistung.au.domain.AuMeldung;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import org.slf4j.Logger;

import java.util.Objects;

public class ErstelleAuMeldungCommandHandler implements ErstelleAuMeldungUseCase {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ErstelleAuMeldungCommandHandler.class);

    private final AuMeldungRepository repository;
    private final LeistungsanspruchPruefungPort anspruchPruefungPort;

    public ErstelleAuMeldungCommandHandler(AuMeldungRepository repository, LeistungsanspruchPruefungPort anspruchPruefungPort) {
        this.repository = repository;
        this.anspruchPruefungPort = anspruchPruefungPort;
    }

    @Override
    public void erstelleAuMeldung(ErstelleAuMeldungCommand command) {
        Objects.requireNonNull(command);
        Svnr svnr = command.svnr();

        boolean hatAnspruch = anspruchPruefungPort.pruefeAnspruch(svnr);
        if (!hatAnspruch) {
            log.info("Kein Leistungsanspruch für SVNR {}, AU-Meldung wird nicht gespeichert", svnr);
        } else {
            speichereAuMeldung(command);
        }
    }

    private void speichereAuMeldung(ErstelleAuMeldungCommand command) {
        AuMeldung meldung = new AuMeldung(command.svnr(), command.auBeginn());
        repository.speichere(meldung);
    }
}
