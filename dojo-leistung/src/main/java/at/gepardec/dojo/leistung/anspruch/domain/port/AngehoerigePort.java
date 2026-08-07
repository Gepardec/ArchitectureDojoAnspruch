package at.gepardec.dojo.leistung.anspruch.domain.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.util.List;

/**
 * Zugang zu den Verwandtschaftsbeziehungen einer Person.
 * <p>
 * Der Port liefert bereits gefiltert nur die Elternteile. Die Kodierung der Beziehungsarten des
 * Umsystems bleibt damit im Adapter und dringt nicht in die Domäne.
 */
public interface AngehoerigePort {

    /** Die Elternteile der Person, niemals {@code null}. */
    List<Svnr> eltern(Svnr svnr);
}
