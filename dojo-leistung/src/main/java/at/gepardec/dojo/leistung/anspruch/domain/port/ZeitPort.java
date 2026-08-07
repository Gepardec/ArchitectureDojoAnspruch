package at.gepardec.dojo.leistung.anspruch.domain.port;

import java.time.LocalDate;

/**
 * Liefert das Tagesdatum.
 * <p>
 * Ausschließlich lesend. Kein {@code setHeute()}, kein {@code reset()}: Ein anderes Datum bekommt
 * man, indem man eine andere Implementierung verdrahtet, nicht indem man gemeinsamen Zustand
 * umschaltet.
 * <p>
 * Der Gegenentwurf steht in {@code feature/solution1}: Dort hat der Port
 * {@code setToday()}/{@code resetToday()} auf einem statischen, veränderlichen Singleton. Damit
 * steht ein Testbelang im Produktionsvertrag, jeder Produktionscode kann das Systemdatum
 * verstellen, und die Tests werden reihenfolgeabhängig.
 * <p>
 * Der Port liegt im Domänenring, obwohl ihn derzeit nur die Application-Schicht nutzt -- alle
 * Ports dieser Lösung liegen dort, was die Architekturregel bei einer Zeile hält. Sobald mehrere
 * Ports ausschließlich von der Application gebraucht werden, gehören sie in ein eigenes
 * Portpaket dieser Schicht.
 */
public interface ZeitPort {

    LocalDate heute();
}
