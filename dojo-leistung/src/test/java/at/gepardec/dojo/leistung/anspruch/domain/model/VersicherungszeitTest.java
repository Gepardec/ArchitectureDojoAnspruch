package at.gepardec.dojo.leistung.anspruch.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class VersicherungszeitTest {

    private static final LocalDate STICHTAG = LocalDate.of(2026, 8, 5);

    @Test
    void offeneZeitIstAktiv() {
        Versicherungszeit zeit = new Versicherungszeit(LocalDate.of(2022, 9, 1), null);

        assertThat(zeit.istAktiv()).isTrue();
    }

    @Test
    void abgeschlosseneZeitIstNichtAktiv() {
        Versicherungszeit zeit = new Versicherungszeit(LocalDate.of(2013, 3, 1), LocalDate.of(2022, 7, 31));

        assertThat(zeit.istAktiv()).isFalse();
    }

    @Test
    void offeneZeitEnthaeltJedenTagAbBeginn() {
        Versicherungszeit zeit = new Versicherungszeit(LocalDate.of(2022, 9, 1), null);

        assertThat(zeit.enthaelt(STICHTAG)).isTrue();
        assertThat(zeit.enthaelt(LocalDate.of(2022, 9, 1))).isTrue();
        assertThat(zeit.enthaelt(LocalDate.of(2022, 8, 31))).isFalse();
    }

    @Test
    void abgeschlosseneZeitEnthaeltNurIhrenZeitraum() {
        Versicherungszeit zeit = new Versicherungszeit(LocalDate.of(2013, 3, 1), LocalDate.of(2022, 7, 31));

        assertThat(zeit.enthaelt(LocalDate.of(2020, 1, 1))).isTrue();
        assertThat(zeit.enthaelt(LocalDate.of(2022, 7, 31))).isTrue();
        assertThat(zeit.enthaelt(LocalDate.of(2022, 8, 1))).isFalse();
    }

    @Test
    void vonIstPflicht() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Versicherungszeit(null, null));
    }

    @Test
    void bisDarfNichtVorVonLiegen() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Versicherungszeit(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 1)));
    }

    @Test
    void stichtagIstPflicht() {
        Versicherungszeit zeit = new Versicherungszeit(LocalDate.of(2022, 9, 1), null);

        assertThatNullPointerException().isThrownBy(() -> zeit.enthaelt(null));
    }
}
