package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.domain.RegelwerkParameterFehltException;
import at.gepardec.dojo.leistung.anspruch.domain.port.RegelwerkPort;

import java.util.Map;
import java.util.Objects;

/**
 * Liest die Regelparameter aus der Ablage der Fachabteilung.
 * <p>
 * Attrappe für die Datenbank: Das Dojo hat keine. Entscheidend für den Architekturvergleich ist
 * die Portgrenze, nicht die Speichertechnologie -- eine echte Datenbank würde ausschließlich
 * diese Klasse ersetzen.
 * <p>
 * Der Vorgabewert 18 steht in der Ablage, NICHT als Fallback im Code. Fehlt der Parameter, bricht
 * die Prüfung ab.
 */
public class RegelwerkAdapter implements RegelwerkPort {

    static final String ALTERSGRENZE_MITVERSICHERUNG = "altersgrenze.mitversicherung";

    /** Entspricht dem Datenbestand, den die Fachabteilung pflegt. */
    private static final Map<String, Integer> VORBELEGUNG =
            Map.of(ALTERSGRENZE_MITVERSICHERUNG, 18);

    private final Map<String, Integer> parameter;

    public RegelwerkAdapter() {
        this(VORBELEGUNG);
    }

    public RegelwerkAdapter(Map<String, Integer> parameter) {
        this.parameter = Map.copyOf(Objects.requireNonNull(parameter, "parameter"));
    }

    @Override
    public int altersgrenzeMitversicherung() {
        Integer wert = parameter.get(ALTERSGRENZE_MITVERSICHERUNG);
        if (wert == null) {
            throw new RegelwerkParameterFehltException(ALTERSGRENZE_MITVERSICHERUNG);
        }
        return wert;
    }
}
