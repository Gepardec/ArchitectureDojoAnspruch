package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.domain.RegelwerkParameterFehltException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RegelwerkAdapterTest {

    @Test
    void liefertDenGepflegtenVorgabewert() {
        assertThat(new RegelwerkAdapter().altersgrenzeMitversicherung()).isEqualTo(18);
    }

    @Test
    void liefertDenGeaendertenWertDerFachabteilung() {
        RegelwerkAdapter adapter =
                new RegelwerkAdapter(Map.of(RegelwerkAdapter.ALTERSGRENZE_MITVERSICHERUNG, 27));

        assertThat(adapter.altersgrenzeMitversicherung()).isEqualTo(27);
    }

    /**
     * Kein stiller Rückfall auf einen Wert im Code -- sonst bliebe eine fehlerhafte
     * Regelwerkspflege unbemerkt und die fachliche Konstante stünde doch wieder im Programm.
     */
    @Test
    void fehlenderParameterBrichtAb() {
        RegelwerkAdapter adapter = new RegelwerkAdapter(Map.of());

        assertThatExceptionOfType(RegelwerkParameterFehltException.class)
                .isThrownBy(adapter::altersgrenzeMitversicherung)
                .withMessageContaining(RegelwerkAdapter.ALTERSGRENZE_MITVERSICHERUNG);
    }
}
