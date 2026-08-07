package at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.leistung.au.domain.AuMeldungService;
import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.au.domain.model.MeldungsErgebnis;
import at.gepardec.dojo.leistung.au.domain.port.AnspruchPruefungPort;
import at.gepardec.dojo.leistung.au.domain.port.AuMeldungPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ErstelleAuMeldungServiceTest {

    private static final LocalDate AU_BEGINN = LocalDate.of(2026, 8, 6);

    private final List<AuMeldung> gespeicherte = new ArrayList<>();
    private final AuMeldungPort auMeldungPort = gespeicherte::add;

    private ErstelleAuMeldungUseCase useCaseMitAnspruch(boolean anspruch) {
        AnspruchPruefungPort anspruchPort = svnr -> anspruch;
        return new ErstelleAuMeldungService(new AuMeldungService(auMeldungPort, anspruchPort));
    }

    @Test
    void uebersetztRohdatenInDasDomaenenmodell() {
        MeldungsErgebnis ergebnis = useCaseMitAnspruch(true)
                .erstelleAuMeldung(TestData.SVNR_KURT, AU_BEGINN);

        assertThat(ergebnis.istGespeichert()).isTrue();
        assertThat(gespeicherte)
                .containsExactly(new AuMeldung(new Svnr(TestData.SVNR_KURT), AU_BEGINN));
    }

    @Test
    void ohneAnspruchWirdNichtsGespeichert() {
        MeldungsErgebnis ergebnis = useCaseMitAnspruch(false)
                .erstelleAuMeldung(TestData.SVNR_EBERHARD, AU_BEGINN);

        assertThat(ergebnis.istGespeichert()).isFalse();
        assertThat(gespeicherte).isEmpty();
    }

    @Test
    void meldungOhneAuBeginnWirdAbgewiesen() {
        assertThatNullPointerException().isThrownBy(
                () -> useCaseMitAnspruch(true).erstelleAuMeldung(TestData.SVNR_KURT, null));

        assertThat(gespeicherte).isEmpty();
    }

    @Test
    void meldungMitUngueltigerSvnrWirdAbgewiesen() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> useCaseMitAnspruch(true).erstelleAuMeldung("1234567890", AU_BEGINN));

        assertThat(gespeicherte).isEmpty();
    }
}
