package at.gepardec.dojo.leistung.anspruch.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Zeitraum einer Versicherung. Ein fehlendes Bis-Datum bedeutet, dass die Zeit noch läuft.
 */
public record Versicherungszeit(LocalDate von, LocalDate bis) {

    public Versicherungszeit {
        Objects.requireNonNull(von, "von");
        if (bis != null && bis.isBefore(von)) {
            throw new IllegalArgumentException("bis (" + bis + ") liegt vor von (" + von + ")");
        }
    }

    /** Eine Versicherungszeit ist aktiv, solange sie kein Bis-Datum hat. */
    public boolean istAktiv() {
        return bis == null;
    }

    public boolean enthaelt(LocalDate stichtag) {
        Objects.requireNonNull(stichtag, "stichtag");
        return !von.isAfter(stichtag) && (bis == null || !stichtag.isAfter(bis));
    }
}
