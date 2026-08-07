package at.gepardec.dojo.leistung.anspruch.application;

import at.gepardec.dojo.leistung.anspruch.domain.AnspruchService;
import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.util.Objects;

/**
 * Dünner Orchestrierer: ermittelt den Stichtag und übergibt an die Domäne. Fachliche
 * Entscheidungen trifft er keine -- die liegen sämtlich in {@code domain.rule}.
 * <p>
 * Der Stichtag kommt seit {@code add-stichtag-port} (Iteration 7) über den {@link ZeitPort}.
 * Damit gibt es in der gesamten Anwendung genau einen Aufruf der Systemuhr, und der steht im
 * dafür vorgesehenen Adapter.
 */
public class PruefeAnspruchService implements PruefeAnspruchUseCase {

    private final AnspruchService anspruchService;
    private final ZeitPort zeitPort;

    public PruefeAnspruchService(AnspruchService anspruchService, ZeitPort zeitPort) {
        this.anspruchService = Objects.requireNonNull(anspruchService, "anspruchService");
        this.zeitPort = Objects.requireNonNull(zeitPort, "zeitPort");
    }

    @Override
    public boolean hatAnspruch(Svnr svnr) {
        Objects.requireNonNull(svnr, "svnr");

        return anspruchService.hatAnspruch(svnr, zeitPort.heute());
    }
}
