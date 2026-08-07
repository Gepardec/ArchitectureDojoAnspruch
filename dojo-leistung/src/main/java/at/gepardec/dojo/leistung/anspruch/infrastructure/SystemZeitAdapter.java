package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitPort;

import java.time.LocalDate;

/**
 * Die einzige Stelle der Anwendung, die die Systemuhr befragt.
 * <p>
 * Die Architekturregel {@code systemuhrNurImZeitAdapter} hält genau das fest.
 */
public class SystemZeitAdapter implements ZeitPort {

    @Override
    public LocalDate heute() {
        return LocalDate.now();
    }
}
