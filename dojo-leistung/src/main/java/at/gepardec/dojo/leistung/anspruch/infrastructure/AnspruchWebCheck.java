package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.application.PruefeAnspruchUseCase;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.util.Objects;

/**
 * Treibender Adapter für UC-ANSP-01 "Anspruch Web Check".
 * <p>
 * Steht stellvertretend für die Web-Oberfläche: Er nimmt die Eingabe entgegen, übersetzt sie in
 * das Domänenmodell und formuliert die Antwort. Er hängt am Input Port, nicht an der
 * Service-Implementierung.
 */
public class AnspruchWebCheck {

    static final String ANSPRUCH_VORHANDEN = "Anspruch vorhanden";
    static final String KEIN_ANSPRUCH = "Kein Anspruch";

    private final PruefeAnspruchUseCase pruefeAnspruch;

    public AnspruchWebCheck(PruefeAnspruchUseCase pruefeAnspruch) {
        this.pruefeAnspruch = Objects.requireNonNull(pruefeAnspruch, "pruefeAnspruch");
    }

    public String pruefe(String eingegebeneSvnr) {
        Svnr svnr = new Svnr(eingegebeneSvnr);

        return pruefeAnspruch.hatAnspruch(svnr) ? ANSPRUCH_VORHANDEN : KEIN_ANSPRUCH;
    }
}
