package at.gepardec.dojo.leistung.anspruch.domain.port;

import at.gepardec.dojo.leistung.anspruch.domain.model.Versicherungszeit;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.util.List;

/**
 * Zugang zu den Versicherungszeiten einer Person.
 * <p>
 * Der Port liegt im Domänenring -- das Definitionsmerkmal von Onion. Die Regel, die ihn braucht,
 * darf ihn deshalb direkt nutzen.
 * <p>
 * Der Port liefert die Zeiten und bewertet sie NICHT. Ob eine Zeit Anspruch begründet, ist eine
 * fachliche Entscheidung und bleibt in der Domäne.
 */
public interface ZeitenPort {

    /** Alle bekannten Versicherungszeiten, niemals {@code null}. */
    List<Versicherungszeit> versicherungszeiten(Svnr svnr);
}
