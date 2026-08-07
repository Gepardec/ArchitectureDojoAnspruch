package at.gepardec.dojo.leistung.au.domain.model;

import java.util.Objects;

/**
 * Ergebnis der Verarbeitung einer AU-Meldung.
 * <p>
 * Bewusst ein Rückgabewert und kein reiner Protokolleintrag: Nebenszenario 1 des Use Case sieht
 * vor, dass ungültige Meldungen in eine Fehler-Queue wandern. Ein Aufrufer, der "gespeichert" und
 * "abgelehnt" nicht unterscheiden kann, könnte das nie umsetzen -- so wie in
 * {@code feature/dojo1}, wo der Use Case {@code void} liefert und nur loggt.
 */
public sealed interface MeldungsErgebnis {

    record Gespeichert(AuMeldung meldung) implements MeldungsErgebnis {
        public Gespeichert {
            Objects.requireNonNull(meldung, "meldung");
        }
    }

    record Abgelehnt(AuMeldung meldung, String grund) implements MeldungsErgebnis {
        public Abgelehnt {
            Objects.requireNonNull(meldung, "meldung");
            Objects.requireNonNull(grund, "grund");
        }
    }

    default boolean istGespeichert() {
        return this instanceof Gespeichert;
    }
}
