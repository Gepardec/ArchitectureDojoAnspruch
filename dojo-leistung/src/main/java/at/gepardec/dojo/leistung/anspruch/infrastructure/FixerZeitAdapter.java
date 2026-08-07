package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitPort;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Liefert ein festes Datum. Damit werden die Testfälle des Dojos zeitstabil, ohne dass
 * gemeinsamer Zustand umgeschaltet werden muss -- die Tests bleiben parallelisierbar.
 */
public class FixerZeitAdapter implements ZeitPort {

    private final LocalDate heute;

    public FixerZeitAdapter(LocalDate heute) {
        this.heute = Objects.requireNonNull(heute, "heute");
    }

    @Override
    public LocalDate heute() {
        return heute;
    }
}
