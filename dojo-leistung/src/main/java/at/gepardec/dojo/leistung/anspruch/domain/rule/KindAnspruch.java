package at.gepardec.dojo.leistung.anspruch.domain.rule;

import at.gepardec.dojo.leistung.anspruch.domain.port.AngehoerigePort;
import at.gepardec.dojo.leistung.anspruch.domain.port.PersonenPort;
import at.gepardec.dojo.leistung.anspruch.domain.port.RegelwerkPort;
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
 * Die Altersgrenze kommt seit {@code add-regelwerk-port} (Iteration 6) aus dem Regelwerk. Die
 * Regel holt sie sich selbst -- die aufrufenden Schichten wissen nichts davon, dass diese Regel
 * überhaupt einen Regelparameter braucht.
 */
public class KindAnspruch implements Anspruch {

    private final PersonenPort personenPort;
    private final AngehoerigePort angehoerigePort;
    private final RegelwerkPort regelwerkPort;
    private final Anspruch eigenAnspruch;

    public KindAnspruch(PersonenPort personenPort, AngehoerigePort angehoerigePort,
                        RegelwerkPort regelwerkPort, Anspruch eigenAnspruch) {
        this.personenPort = Objects.requireNonNull(personenPort, "personenPort");
        this.angehoerigePort = Objects.requireNonNull(angehoerigePort, "angehoerigePort");
        this.regelwerkPort = Objects.requireNonNull(regelwerkPort, "regelwerkPort");
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
     * "Unter" der Grenze, nicht "bis einschließlich": Am Tag, an dem die Grenze erreicht wird,
     * endet der Kindanspruch.
     */
    private boolean istUnterAltersgrenze(LocalDate geburtsdatum, LocalDate stichtag) {
        int altersgrenze = regelwerkPort.altersgrenzeMitversicherung();
        return geburtsdatum.plusYears(altersgrenze).isAfter(stichtag);
    }
}
