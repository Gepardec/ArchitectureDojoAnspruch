package at.gepardec.dojo.leistung.infrastructure;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.leistung.anspruch.application.PruefeAnspruchService;
import at.gepardec.dojo.leistung.anspruch.application.PruefeAnspruchUseCase;
import at.gepardec.dojo.leistung.anspruch.domain.AnspruchService;
import at.gepardec.dojo.leistung.anspruch.domain.port.AngehoerigePort;
import at.gepardec.dojo.leistung.anspruch.domain.port.PersonenPort;
import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitPort;
import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitenPort;
import at.gepardec.dojo.leistung.anspruch.domain.rule.Anspruch;
import at.gepardec.dojo.leistung.anspruch.domain.rule.EigenAnspruch;
import at.gepardec.dojo.leistung.anspruch.domain.rule.KindAnspruch;
import at.gepardec.dojo.leistung.anspruch.infrastructure.AngehoerigeAdapter;
import at.gepardec.dojo.leistung.anspruch.infrastructure.SystemZeitAdapter;
import at.gepardec.dojo.leistung.anspruch.infrastructure.AnspruchWebCheck;
import at.gepardec.dojo.leistung.anspruch.infrastructure.PersonenAdapter;
import at.gepardec.dojo.leistung.anspruch.infrastructure.ZeitenAdapter;
import at.gepardec.dojo.leistung.au.application.ErstelleAuMeldungService;
import at.gepardec.dojo.leistung.au.application.ErstelleAuMeldungUseCase;
import at.gepardec.dojo.leistung.au.domain.AuMeldungService;
import at.gepardec.dojo.leistung.au.domain.port.AnspruchPruefungPort;
import at.gepardec.dojo.leistung.au.infrastructure.AnspruchPruefungAdapter;
import at.gepardec.dojo.leistung.au.infrastructure.AuMeldungAdapter;
import at.gepardec.dojo.personen.PersonenService;
import at.gepardec.dojo.zeiten.ZeitenService;

import java.util.List;
import java.util.Objects;

/**
 * Verdrahtet die Anwendung. Die einzige Klasse, die alle Ringe kennen darf -- deshalb liegt sie
 * im äußersten.
 * <p>
 * Der Gegenentwurf steht in {@code feature/solution1}: dort liegt die Factory im Port-Package und
 * importiert sämtliche Adapter. Damit hängt die Portschicht an der Adapterschicht, und der
 * Domänenring erreicht über sie transitiv die Umsysteme -- ein Verstoß gegen die Dependency Rule
 * jeder der drei Varianten.
 * <p>
 * Alle Abhängigkeiten gehen über Konstruktoren. Kein Service Locator, kein globaler Zustand:
 * Tests bauen sich denselben Graphen mit eigenen Ports zusammen, ohne hier etwas umschalten zu
 * müssen.
 */
public class CompositionRoot {

    private final AnspruchWebCheck anspruchWebCheck;
    private final ErstelleAuMeldungUseCase erstelleAuMeldung;
    private final AuMeldungAdapter auMeldungAdapter;

    /** Regelbetrieb: Der Stichtag kommt aus der Systemuhr. */
    public CompositionRoot() {
        this(new SystemZeitAdapter());
    }

    /**
     * Verdrahtung mit einer wählbaren Zeitquelle. Tests übergeben hier einen
     * {@code FixerZeitAdapter} und werden damit zeitstabil -- ohne globalen Zustand umzuschalten.
     */
    public CompositionRoot(ZeitPort zeitPort) {
        Objects.requireNonNull(zeitPort, "zeitPort");

        // --- Anspruch: Adapter an die Umsysteme ---
        ZeitenPort zeitenPort = new ZeitenAdapter(new ZeitenService());
        PersonenPort personenPort = new PersonenAdapter(new PersonenService());
        AngehoerigePort angehoerigePort = new AngehoerigeAdapter(new AngehoerigeService());

        // --- Anspruch: Regelkatalog ---
        // Eine weitere Anspruchsart wird hier ergaenzt; bestehende Klassen bleiben unberuehrt.
        Anspruch eigenAnspruch = new EigenAnspruch(zeitenPort);
        Anspruch kindAnspruch = new KindAnspruch(personenPort, angehoerigePort, eigenAnspruch);
        AnspruchService anspruchService = new AnspruchService(List.of(eigenAnspruch, kindAnspruch));

        PruefeAnspruchUseCase pruefeAnspruch = new PruefeAnspruchService(anspruchService, zeitPort);
        this.anspruchWebCheck = new AnspruchWebCheck(pruefeAnspruch);

        // --- AU: eigener Port an der Kontextgrenze ---
        this.auMeldungAdapter = new AuMeldungAdapter();
        AnspruchPruefungPort anspruchPruefungPort = new AnspruchPruefungAdapter(pruefeAnspruch);
        AuMeldungService auMeldungService = new AuMeldungService(auMeldungAdapter, anspruchPruefungPort);
        this.erstelleAuMeldung = new ErstelleAuMeldungService(auMeldungService);
    }

    public AnspruchWebCheck anspruchWebCheck() {
        return anspruchWebCheck;
    }

    public ErstelleAuMeldungUseCase erstelleAuMeldung() {
        return erstelleAuMeldung;
    }

    /** Zugriff auf die Ablage, damit Tests die Speicherung nachweisen können. */
    public AuMeldungAdapter auMeldungAblage() {
        return auMeldungAdapter;
    }
}
