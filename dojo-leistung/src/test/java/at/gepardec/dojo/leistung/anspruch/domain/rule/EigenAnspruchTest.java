package at.gepardec.dojo.leistung.anspruch.domain.rule;

import at.gepardec.dojo.leistung.anspruch.domain.model.Versicherungszeit;
import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitenPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EigenAnspruchTest {

    private static final LocalDate STICHTAG = LocalDate.of(2026, 8, 5);
    private static final Svnr KURT = new Svnr(TestData.SVNR_KURT);

    @Mock
    private ZeitenPort zeitenPort;

    private EigenAnspruch eigenAnspruch;

    @BeforeEach
    void setUp() {
        eigenAnspruch = new EigenAnspruch(zeitenPort);
    }

    @Test
    void aktiveVersicherungszeitBegruendetAnspruch() {
        when(zeitenPort.versicherungszeiten(KURT))
                .thenReturn(List.of(new Versicherungszeit(LocalDate.of(2022, 9, 1), null)));

        assertThat(eigenAnspruch.anspruch(KURT, STICHTAG)).isTrue();
    }

    @Test
    void nurAbgelaufeneZeitenBegruendenKeinenAnspruch() {
        when(zeitenPort.versicherungszeiten(KURT))
                .thenReturn(List.of(new Versicherungszeit(LocalDate.of(2013, 3, 1), LocalDate.of(2022, 7, 31))));

        assertThat(eigenAnspruch.anspruch(KURT, STICHTAG)).isFalse();
    }

    @Test
    void keineVersicherungszeitenBegruendenKeinenAnspruch() {
        when(zeitenPort.versicherungszeiten(KURT)).thenReturn(List.of());

        assertThat(eigenAnspruch.anspruch(KURT, STICHTAG)).isFalse();
    }

    @Test
    void eineVonMehrerenAktivenZeitenGenuegt() {
        when(zeitenPort.versicherungszeiten(KURT)).thenReturn(List.of(
                new Versicherungszeit(LocalDate.of(2013, 3, 1), LocalDate.of(2022, 7, 31)),
                new Versicherungszeit(LocalDate.of(2022, 9, 1), null)));

        assertThat(eigenAnspruch.anspruch(KURT, STICHTAG)).isTrue();
    }

    @Test
    void aktiveZeitVorIhremBeginnBegruendetKeinenAnspruch() {
        when(zeitenPort.versicherungszeiten(KURT))
                .thenReturn(List.of(new Versicherungszeit(LocalDate.of(2022, 9, 1), null)));

        assertThat(eigenAnspruch.anspruch(KURT, LocalDate.of(2020, 1, 1))).isFalse();
    }
}
