package at.gepardec.dojo.leistung;

import at.gepardec.dojo.leistung.anspruch.infrastructure.FixerZeitAdapter;
import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.au.domain.model.MeldungsErgebnis;
import at.gepardec.dojo.leistung.infrastructure.CompositionRoot;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Durchgängiger Test über den echten Objektgraphen -- von den treibenden Adaptern bis zu den
 * Umsystem-Attrappen, ohne Test-Doubles.
 * <p>
 * Die Klasse endet auf {@code Test} und läuft damit im normalen {@code mvn test} mit. In
 * {@code feature/dojo1} heißt der entsprechende Test {@code ...SIT}, wird von Surefire nicht
 * erfasst und lief mangels Failsafe-Konfiguration nie.
 * <p>
 * <b>Zeitstabil seit Iteration 7:</b> Der Stichtag ist mit dem {@link FixerZeitAdapter} auf den
 * 05.08.2026 festgelegt. Die Ergebnisse ändern sich damit nicht mehr dadurch, dass die Familie
 * Leonhardsberger älter wird.
 */
class LeistungIntegrationTest {

    private static final LocalDate STICHTAG = LocalDate.of(2026, 8, 5);
    private static final LocalDate AU_BEGINN = LocalDate.of(2026, 8, 6);

    private CompositionRoot anwendung;

    @BeforeEach
    void setUp() {
        anwendung = new CompositionRoot(new FixerZeitAdapter(STICHTAG));
    }

    @Test
    void kurtHatAnspruchDurchAktiveVersicherungszeit() {
        assertThat(anwendung.anspruchWebCheck().pruefe(TestData.SVNR_KURT))
                .isEqualTo("Anspruch vorhanden");
    }

    @Test
    void angieHatAnspruchAlsMitversichertesKind() {
        assertThat(anwendung.anspruchWebCheck().pruefe(TestData.SVNR_ANGIE))
                .isEqualTo("Anspruch vorhanden");
    }

    @Test
    void eberhardHatKeinenAnspruch() {
        assertThat(anwendung.anspruchWebCheck().pruefe(TestData.SVNR_EBERHARD))
                .isEqualTo("Kein Anspruch");
    }

    @Test
    void mariaHatKeinenAnspruch() {
        // Ehepartnerin ohne eigene Versicherungszeit: Die Mitversicherung von Ehepartnern ist
        // keine Geschaeftsregel dieser Iterationen.
        assertThat(anwendung.anspruchWebCheck().pruefe(TestData.SVNR_MARIA))
                .isEqualTo("Kein Anspruch");
    }

    @Test
    void kurtsKrankmeldungWirdGespeichert() {
        MeldungsErgebnis ergebnis =
                anwendung.erstelleAuMeldung().erstelleAuMeldung(TestData.SVNR_KURT, AU_BEGINN);

        assertThat(ergebnis.istGespeichert()).isTrue();
        assertThat(anwendung.auMeldungAblage().gespeicherte())
                .containsExactly(new AuMeldung(new Svnr(TestData.SVNR_KURT), AU_BEGINN));
    }

    @Test
    void eberhardsKrankmeldungWirdAbgelehnt() {
        MeldungsErgebnis ergebnis =
                anwendung.erstelleAuMeldung().erstelleAuMeldung(TestData.SVNR_EBERHARD, AU_BEGINN);

        assertThat(ergebnis.istGespeichert()).isFalse();
        assertThat(anwendung.auMeldungAblage().gespeicherte()).isEmpty();
    }

    /**
     * Der Nachweis, dass die Zeitabhängigkeit selbst geprüft ist und nicht nur wegkonfiguriert:
     * 2029 ist Angie 19 und damit über der Altersgrenze.
     */
    @Test
    void angieHatMitNeunzehnKeinenAnspruchMehr() {
        CompositionRoot spaeter = new CompositionRoot(new FixerZeitAdapter(LocalDate.of(2029, 8, 5)));

        assertThat(spaeter.anspruchWebCheck().pruefe(TestData.SVNR_ANGIE))
                .isEqualTo("Kein Anspruch");
    }

    @Test
    void angiesKrankmeldungWirdGespeichertWeilSieMitversichertIst() {
        MeldungsErgebnis ergebnis =
                anwendung.erstelleAuMeldung().erstelleAuMeldung(TestData.SVNR_ANGIE, AU_BEGINN);

        assertThat(ergebnis.istGespeichert()).isTrue();
    }
}
