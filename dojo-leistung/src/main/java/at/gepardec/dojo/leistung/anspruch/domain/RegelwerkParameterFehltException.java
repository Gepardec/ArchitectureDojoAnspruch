package at.gepardec.dojo.leistung.anspruch.domain;

/**
 * Ein Regelparameter ist im Regelwerk nicht hinterlegt.
 * <p>
 * Bewusst ein Abbruch und kein stiller Rückfall auf einen Vorgabewert im Code: Ein Fallback würde
 * bedeuten, dass eine fehlerhafte Regelwerkspflege unbemerkt bleibt und die fachliche Konstante
 * doch wieder im Programm steht -- also genau das, was Iteration 6 abschaffen soll.
 */
public class RegelwerkParameterFehltException extends RuntimeException {

    public RegelwerkParameterFehltException(String parameter) {
        super("Regelparameter nicht im Regelwerk hinterlegt: " + parameter);
    }
}
