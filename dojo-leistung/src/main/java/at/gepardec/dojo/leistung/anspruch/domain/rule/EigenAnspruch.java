package at.gepardec.dojo.leistung.anspruch.domain.rule;

import at.gepardec.dojo.leistung.anspruch.domain.model.Versicherungszeit;
import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitenPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Geschäftsregel: Wer zum Stichtag eine aktive Versicherungszeit hat, hat Anspruch.
 * <p>
 * Die Regel besorgt sich ihre Daten selbst über den Port. Genau dieses Zugeständnis ist der
 * Grund, warum für diese Fachlichkeit Onion und nicht Clean Architecture gewählt wurde: Eine
 * weitere Anspruchsart bringt ihren eigenen Datenbedarf mit, ohne dass eine aufrufende Schicht
 * davon wissen muss.
 */
public class EigenAnspruch implements Anspruch {

    private final ZeitenPort zeitenPort;

    public EigenAnspruch(ZeitenPort zeitenPort) {
        this.zeitenPort = Objects.requireNonNull(zeitenPort, "zeitenPort");
    }

    @Override
    public boolean anspruch(Svnr svnr, LocalDate stichtag) {
        Objects.requireNonNull(svnr, "svnr");
        Objects.requireNonNull(stichtag, "stichtag");

        return zeitenPort.versicherungszeiten(svnr).stream()
                .anyMatch(zeit -> begruendetAnspruch(zeit, stichtag));
    }

    private boolean begruendetAnspruch(Versicherungszeit zeit, LocalDate stichtag) {
        return zeit.istAktiv() && zeit.enthaelt(stichtag);
    }
}
