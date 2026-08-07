package at.gepardec.dojo.leistung.anspruch.application;

import at.gepardec.dojo.leistung.anspruch.domain.AnspruchService;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Dünner Orchestrierer: ermittelt den Stichtag und übergibt an die Domäne. Fachliche
 * Entscheidungen trifft er keine -- die liegen sämtlich in {@code domain.rule}.
 * <p>
 * Der Aufruf von {@link LocalDate#now()} ist der letzte verbliebene Uhrzugriff der Anwendung.
 * Der Change {@code add-stichtag-port} (Iteration 7) ersetzt ihn durch einen Port. Der Diff zeigt
 * dann, was ein vorgebbares Tagesdatum in dieser Variante kostet.
 */
public class PruefeAnspruchService implements PruefeAnspruchUseCase {

    private final AnspruchService anspruchService;

    public PruefeAnspruchService(AnspruchService anspruchService) {
        this.anspruchService = Objects.requireNonNull(anspruchService, "anspruchService");
    }

    @Override
    public boolean hatAnspruch(Svnr svnr) {
        Objects.requireNonNull(svnr, "svnr");

        return anspruchService.hatAnspruch(svnr, LocalDate.now());
    }
}
