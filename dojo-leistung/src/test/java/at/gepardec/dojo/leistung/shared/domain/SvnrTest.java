package at.gepardec.dojo.leistung.shared.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SvnrTest {

    @Test
    void gueltigeNummerWirdAngenommen() {
        Svnr svnr = new Svnr(TestData.SVNR_KURT);

        assertThat(svnr.nummer()).isEqualTo(TestData.SVNR_KURT);
    }

    @Test
    void falschePruefzifferWirdAbgelehnt() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Svnr("1234567890"))
                .withMessageContaining("1234567890");
    }

    @Test
    void nullWirdAbgelehnt() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Svnr(null));
    }

    @Test
    void falscheLaengeWirdAbgelehnt() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Svnr("145425038"));
    }

    @Test
    void wertsemantikIstVorhanden() {
        Svnr eine = new Svnr(TestData.SVNR_KURT);
        Svnr andere = new Svnr(TestData.SVNR_KURT);

        assertThat(eine).isEqualTo(andere);
        assertThat(eine).hasSameHashCodeAs(andere);
    }
}
