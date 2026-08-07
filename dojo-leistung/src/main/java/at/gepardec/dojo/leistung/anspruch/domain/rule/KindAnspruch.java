package at.gepardec.dojo.leistung.anspruch.domain.rule;

import at.gepardec.dojo.leistung.anspruch.domain.port.AngehoerigePort;
import at.gepardec.dojo.leistung.anspruch.domain.port.PersonenPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Geschäftsregel: Kinder unter der Altersgrenze haben Anspruch, wenn mindestens ein Elternteil
 * einen Eigenanspruch hat.
 * <p>
 * Beim Elternteil wird ausdrücklich nur der {@link EigenAnspruch} geprüft, nicht der volle
 * Anspruch. Ein Elternteil, das selbst nur mitversichert ist, begründet keinen Anspruch des
 * Kindes.
 * <p>
 * Die Altersgrenze steht vorerst als Konstante hier. Der Change {@code add-regelwerk-port}
 * (Iteration 6) ersetzt sie durch einen Port -- der Diff zeigt dann, was ein konfigurierbares
 * Regelwerk in dieser Variante kostet.
 */
public class KindAnspruch implements Anspruch {

    static final int ALTERSGRENZE_JAHRE = 18;

    private final PersonenPort personenPort;
    private final AngehoerigePort angehoerigePort;
    private final Anspruch eigenAnspruch;

    public KindAnspruch(PersonenPort personenPort, AngehoerigePort angehoerigePort, Anspruch eigenAnspruch) {
        this.personenPort = Objects.requireNonNull(personenPort, "personenPort");
        this.angehoerigePort = Objects.requireNonNull(angehoerigePort, "angehoerigePort");
        this.eigenAnspruch = Objects.requireNonNull(eigenAnspruch, "eigenAnspruch");
    }

    @Override
    public boolean anspruch(Svnr svnr, LocalDate stichtag) {
        Objects.requireNonNull(svnr, "svnr");
        Objects.requireNonNull(stichtag, "stichtag");

        Optional<LocalDate> geburtsdatum = personenPort.geburtsdatum(svnr);
        if (geburtsdatum.isEmpty()) {
            return false;
        }
        if (!istUnterAltersgrenze(geburtsdatum.get(), stichtag)) {
            return false;
        }

        return angehoerigePort.eltern(svnr).stream()
                .anyMatch(elternteil -> eigenAnspruch.anspruch(elternteil, stichtag));
    }

    /**
     * "Unter" der Grenze, nicht "bis einschließlich": Am Tag des 18. Geburtstags ist die Grenze
     * erreicht und der Kindanspruch endet.
     */
    private boolean istUnterAltersgrenze(LocalDate geburtsdatum, LocalDate stichtag) {
        return geburtsdatum.plusYears(ALTERSGRENZE_JAHRE).isAfter(stichtag);
    }
}
