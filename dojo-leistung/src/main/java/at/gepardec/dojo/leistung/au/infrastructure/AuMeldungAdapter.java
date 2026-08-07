package at.gepardec.dojo.leistung.au.infrastructure;

import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.au.domain.port.AuMeldungPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Ablage der AU-Meldungen.
 * <p>
 * Attrappe: Das Dojo hat keine Datenbank. Die Meldungen werden im Speicher gehalten, damit Tests
 * die Speicherung tatsächlich nachweisen können statt nur ein Log zu erzeugen. Eine echte
 * Persistenz würde ausschließlich diese Klasse ersetzen.
 */
public class AuMeldungAdapter implements AuMeldungPort {

    private static final Logger log = LoggerFactory.getLogger(AuMeldungAdapter.class);

    private final List<AuMeldung> gespeicherte = new ArrayList<>();

    @Override
    public void speichere(AuMeldung meldung) {
        gespeicherte.add(meldung);
        log.info("AU-Meldung gespeichert: {} ab {}", meldung.svnr(), meldung.auBeginn());
    }

    public List<AuMeldung> gespeicherte() {
        return List.copyOf(gespeicherte);
    }
}
