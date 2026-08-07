package at.gepardec.dojo.leistung.au.infrastructure;

import at.gepardec.dojo.leistung.anspruch.application.PruefeAnspruchUseCase;
import at.gepardec.dojo.leistung.au.domain.port.AnspruchPruefungPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.util.Objects;

/**
 * Die einzige Stelle im AU-Kontext, die den Anspruchskontext kennen darf.
 * <p>
 * Genau diese Beschränkung prüft die Architekturregel
 * {@code auKenntAnspruchNurAusDerInfrastruktur}.
 */
public class AnspruchPruefungAdapter implements AnspruchPruefungPort {

    private final PruefeAnspruchUseCase pruefeAnspruch;

    public AnspruchPruefungAdapter(PruefeAnspruchUseCase pruefeAnspruch) {
        this.pruefeAnspruch = Objects.requireNonNull(pruefeAnspruch, "pruefeAnspruch");
    }

    @Override
    public boolean hatAnspruch(Svnr svnr) {
        return pruefeAnspruch.hatAnspruch(svnr);
    }
}
