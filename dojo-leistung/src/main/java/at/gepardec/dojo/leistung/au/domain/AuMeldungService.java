package at.gepardec.dojo.leistung.au.domain;

import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.au.domain.model.MeldungsErgebnis;
import at.gepardec.dojo.leistung.au.domain.port.AnspruchPruefungPort;
import at.gepardec.dojo.leistung.au.domain.port.AuMeldungPort;

import java.util.Objects;

/**
 * Geschäftsregel: Eine AU-Meldung ist nur gültig und darf nur gespeichert werden, wenn für die
 * gemeldete Person ein Leistungsanspruch besteht.
 * <p>
 * Die Regel liegt bewusst hier und nicht im Application Service. In {@code feature/dojo1} steht
 * dieselbe Entscheidung als {@code if} im Interactor -- ein Orchestrierer, der Fachregeln
 * auswertet.
 */
public class AuMeldungService {

    static final String GRUND_KEIN_ANSPRUCH = "Kein Leistungsanspruch zum Zeitpunkt der Meldung";

    private final AuMeldungPort auMeldungPort;
    private final AnspruchPruefungPort anspruchPruefungPort;

    public AuMeldungService(AuMeldungPort auMeldungPort, AnspruchPruefungPort anspruchPruefungPort) {
        this.auMeldungPort = Objects.requireNonNull(auMeldungPort, "auMeldungPort");
        this.anspruchPruefungPort = Objects.requireNonNull(anspruchPruefungPort, "anspruchPruefungPort");
    }

    public MeldungsErgebnis melde(AuMeldung meldung) {
        Objects.requireNonNull(meldung, "meldung");

        if (!anspruchPruefungPort.hatAnspruch(meldung.svnr())) {
            return new MeldungsErgebnis.Abgelehnt(meldung, GRUND_KEIN_ANSPRUCH);
        }

        auMeldungPort.speichere(meldung);
        return new MeldungsErgebnis.Gespeichert(meldung);
    }
}
