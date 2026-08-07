package at.gepardec.dojo.leistung.anspruch.domain.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Zugang zu den Stammdaten einer Person.
 * <p>
 * Das Geburtsdatum ist bewusst {@link Optional}: Zu einer unbekannten Versicherungsnummer gibt es
 * keines. Ein {@code null} an dieser Stelle ist die Ursache dafür, dass beide bestehenden
 * Lösungen bei unbekannter SVNR mit einer NullPointerException abbrechen.
 */
public interface PersonenPort {

    Optional<LocalDate> geburtsdatum(Svnr svnr);
}
