package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.application.PruefeAnspruchUseCase;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AnspruchWebCheckTest {

    private static final PruefeAnspruchUseCase HAT_ANSPRUCH = svnr -> true;
    private static final PruefeAnspruchUseCase HAT_KEINEN_ANSPRUCH = svnr -> false;

    @Test
    void meldetVorhandenenAnspruch() {
        AnspruchWebCheck webCheck = new AnspruchWebCheck(HAT_ANSPRUCH);

        assertThat(webCheck.pruefe(TestData.SVNR_KURT)).isEqualTo("Anspruch vorhanden");
    }

    @Test
    void meldetFehlendenAnspruch() {
        AnspruchWebCheck webCheck = new AnspruchWebCheck(HAT_KEINEN_ANSPRUCH);

        assertThat(webCheck.pruefe(TestData.SVNR_EBERHARD)).isEqualTo("Kein Anspruch");
    }

    @Test
    void reichtDieEingabeAlsWertobjektWeiter() {
        Svnr[] empfangen = new Svnr[1];
        AnspruchWebCheck webCheck = new AnspruchWebCheck(svnr -> {
            empfangen[0] = svnr;
            return true;
        });

        webCheck.pruefe(TestData.SVNR_ANGIE);

        assertThat(empfangen[0]).isEqualTo(new Svnr(TestData.SVNR_ANGIE));
    }

    @Test
    void ungueltigeEingabeWirdAbgewiesen() {
        AnspruchWebCheck webCheck = new AnspruchWebCheck(HAT_ANSPRUCH);

        assertThatIllegalArgumentException().isThrownBy(() -> webCheck.pruefe("keine-svnr"));
    }
}
