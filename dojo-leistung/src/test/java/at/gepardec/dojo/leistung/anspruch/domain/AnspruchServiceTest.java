package at.gepardec.dojo.leistung.anspruch.domain;

import at.gepardec.dojo.leistung.anspruch.domain.rule.Anspruch;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnspruchServiceTest {

    private static final LocalDate STICHTAG = LocalDate.of(2026, 8, 5);
    private static final Svnr IRGENDWER = new Svnr(TestData.SVNR_KURT);

    private static final Anspruch GREIFT = (svnr, stichtag) -> true;
    private static final Anspruch GREIFT_NICHT = (svnr, stichtag) -> false;

    @Test
    void ohneRegelnBestehtKeinAnspruch() {
        AnspruchService service = new AnspruchService(List.of());

        assertThat(service.hatAnspruch(IRGENDWER, STICHTAG)).isFalse();
    }

    @Test
    void eineGreifendeRegelGenuegt() {
        AnspruchService service = new AnspruchService(List.of(GREIFT_NICHT, GREIFT));

        assertThat(service.hatAnspruch(IRGENDWER, STICHTAG)).isTrue();
    }

    @Test
    void keineGreifendeRegelBedeutetKeinAnspruch() {
        AnspruchService service = new AnspruchService(List.of(GREIFT_NICHT, GREIFT_NICHT));

        assertThat(service.hatAnspruch(IRGENDWER, STICHTAG)).isFalse();
    }

    /**
     * Beleg für die Erweiterbarkeit: Eine zusätzliche Anspruchsart ist eine weitere
     * Implementierung in der Liste, ohne Änderung an dieser Klasse oder an bestehenden Regeln.
     */
    @Test
    void weitereAnspruchsartWirdOhneCodeaenderungWirksam() {
        Anspruch neueAnspruchsart = (svnr, stichtag) -> svnr.equals(IRGENDWER);
        AnspruchService service = new AnspruchService(List.of(GREIFT_NICHT, neueAnspruchsart));

        assertThat(service.hatAnspruch(IRGENDWER, STICHTAG)).isTrue();
    }
}
