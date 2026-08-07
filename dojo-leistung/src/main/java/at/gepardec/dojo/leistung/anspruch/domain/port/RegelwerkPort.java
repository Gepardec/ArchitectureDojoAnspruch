package at.gepardec.dojo.leistung.anspruch.domain.port;

import at.gepardec.dojo.leistung.anspruch.domain.RegelwerkParameterFehltException;

/**
 * Zugang zum Regelwerk der Anspruchsprüfung, das die Fachabteilung pflegt.
 * <p>
 * Fachlich benannte Zugriffe statt eines generischen {@code String get(String key)}: Ein
 * generischer Zugriff würde die Fachlichkeit in Zeichenketten verstecken und Tippfehler erst zur
 * Laufzeit sichtbar machen. Der Preis ist, dass jeder neue Parameter das Interface erweitert --
 * vertretbar, solange das Regelwerk überschaubar bleibt.
 * <p>
 * Der Port liegt im Domänenring, damit die Regeln ihn direkt nutzen können. Genau dieses
 * Zugeständnis ist der Grund, warum für diese Fachlichkeit Onion gewählt wurde: Unter Clean
 * Architecture müsste stattdessen der Interactor den Wert beschaffen und dafür wissen, welche
 * Regel welche Daten braucht.
 *
 * @throws RegelwerkParameterFehltException wenn der Parameter nicht hinterlegt ist
 */
public interface RegelwerkPort {

    int altersgrenzeMitversicherung();
}
