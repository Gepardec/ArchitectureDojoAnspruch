package at.gepardec.dojo.leistung.anspruch.domain.rule;

import at.gepardec.dojo.leistung.anspruch.domain.port.AngehoerigePort;
import at.gepardec.dojo.leistung.anspruch.domain.port.PersonenPort;
import at.gepardec.dojo.leistung.anspruch.domain.port.RegelwerkPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KindAnspruchTest {

    private static final LocalDate STICHTAG = LocalDate.of(2026, 8, 5);
    private static final Svnr ANGIE = new Svnr(TestData.SVNR_ANGIE);
    private static final Svnr EBERHARD = new Svnr(TestData.SVNR_EBERHARD);
    private static final Svnr KURT = new Svnr(TestData.SVNR_KURT);
    private static final Svnr MARIA = new Svnr(TestData.SVNR_MARIA);

    private static final LocalDate GEBURT_ANGIE = LocalDate.of(2010, 7, 24);
    private static final LocalDate GEBURT_EBERHARD = LocalDate.of(2002, 4, 1);

    @Mock
    private PersonenPort personenPort;
    @Mock
    private AngehoerigePort angehoerigePort;
    @Mock
    private RegelwerkPort regelwerkPort;
    @Mock
    private Anspruch eigenAnspruch;

    private KindAnspruch kindAnspruch;

    @BeforeEach
    void setUp() {
        kindAnspruch = new KindAnspruch(personenPort, angehoerigePort, regelwerkPort, eigenAnspruch);
        lenient().when(regelwerkPort.altersgrenzeMitversicherung()).thenReturn(18);
    }

    @Test
    void kindMitEigenanspruchsberechtigtemElternteilHatAnspruch() {
        when(personenPort.geburtsdatum(ANGIE)).thenReturn(Optional.of(GEBURT_ANGIE));
        when(angehoerigePort.eltern(ANGIE)).thenReturn(List.of(MARIA, KURT));
        lenient().when(eigenAnspruch.anspruch(MARIA, STICHTAG)).thenReturn(false);
        when(eigenAnspruch.anspruch(KURT, STICHTAG)).thenReturn(true);

        assertThat(kindAnspruch.anspruch(ANGIE, STICHTAG)).isTrue();
    }

    @Test
    void volljaehrigesKindHatKeinenAnspruch() {
        when(personenPort.geburtsdatum(EBERHARD)).thenReturn(Optional.of(GEBURT_EBERHARD));

        assertThat(kindAnspruch.anspruch(EBERHARD, STICHTAG)).isFalse();
    }

    /**
     * Der Randfall, an dem {@code feature/solution1} scheitert: dort ist die Prüfung
     * {@code alter > 18}, sodass ein exakt 18-Jähriger den Kindanspruch behält.
     */
    @Test
    void amAchtzehntenGeburtstagEndetDerKindanspruch() {
        LocalDate achtzehnterGeburtstag = GEBURT_ANGIE.plusYears(18);
        when(personenPort.geburtsdatum(ANGIE)).thenReturn(Optional.of(GEBURT_ANGIE));

        assertThat(kindAnspruch.anspruch(ANGIE, achtzehnterGeburtstag)).isFalse();
    }

    @Test
    void amTagVorDemAchtzehntenGeburtstagBestehtNochAnspruch() {
        LocalDate tagDavor = GEBURT_ANGIE.plusYears(18).minusDays(1);
        when(personenPort.geburtsdatum(ANGIE)).thenReturn(Optional.of(GEBURT_ANGIE));
        when(angehoerigePort.eltern(ANGIE)).thenReturn(List.of(KURT));
        when(eigenAnspruch.anspruch(KURT, tagDavor)).thenReturn(true);

        assertThat(kindAnspruch.anspruch(ANGIE, tagDavor)).isTrue();
    }

    @Test
    void ohneElternteilMitEigenanspruchKeinAnspruch() {
        when(personenPort.geburtsdatum(ANGIE)).thenReturn(Optional.of(GEBURT_ANGIE));
        when(angehoerigePort.eltern(ANGIE)).thenReturn(List.of(MARIA, KURT));
        when(eigenAnspruch.anspruch(any(), any())).thenReturn(false);

        assertThat(kindAnspruch.anspruch(ANGIE, STICHTAG)).isFalse();
    }

    @Test
    void ohneElternKeinAnspruch() {
        when(personenPort.geburtsdatum(ANGIE)).thenReturn(Optional.of(GEBURT_ANGIE));
        when(angehoerigePort.eltern(ANGIE)).thenReturn(List.of());

        assertThat(kindAnspruch.anspruch(ANGIE, STICHTAG)).isFalse();
    }

    /**
     * Iteration 6: Die Fachabteilung hebt die Altersgrenze an. Eberhard (24 am Stichtag) fällt
     * damit unter die Mitversicherung -- ohne dass Programmcode geändert wurde.
     */
    @Test
    void angehobeneAltersgrenzeWirktSofort() {
        when(regelwerkPort.altersgrenzeMitversicherung()).thenReturn(27);
        when(personenPort.geburtsdatum(EBERHARD)).thenReturn(Optional.of(GEBURT_EBERHARD));
        when(angehoerigePort.eltern(EBERHARD)).thenReturn(List.of(KURT));
        when(eigenAnspruch.anspruch(KURT, STICHTAG)).thenReturn(true);

        assertThat(kindAnspruch.anspruch(EBERHARD, STICHTAG)).isTrue();
    }

    @Test
    void unbekanntePersonFuehrtNichtZumFehler() {
        when(personenPort.geburtsdatum(ANGIE)).thenReturn(Optional.empty());

        assertThat(kindAnspruch.anspruch(ANGIE, STICHTAG)).isFalse();
    }
}
