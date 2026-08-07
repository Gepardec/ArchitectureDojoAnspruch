package at.gepardec.dojo.leistung.anspruch.domain;

import at.gepardec.dojo.leistung.anspruch.domain.rule.Anspruch;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Wertet den Katalog der Anspruchsregeln aus. Anspruch besteht, sobald eine Regel greift.
 * <p>
 * Eine weitere Anspruchsart hinzuzufügen heißt: eine {@link Anspruch}-Implementierung schreiben
 * und sie bei der Verdrahtung in die Liste aufnehmen. Diese Klasse bleibt unverändert.
 */
public class AnspruchService {

    private final List<Anspruch> regeln;

    public AnspruchService(List<Anspruch> regeln) {
        this.regeln = List.copyOf(Objects.requireNonNull(regeln, "regeln"));
    }

    public boolean hatAnspruch(Svnr svnr, LocalDate stichtag) {
        Objects.requireNonNull(svnr, "svnr");
        Objects.requireNonNull(stichtag, "stichtag");

        return regeln.stream().anyMatch(regel -> regel.anspruch(svnr, stichtag));
    }
}
