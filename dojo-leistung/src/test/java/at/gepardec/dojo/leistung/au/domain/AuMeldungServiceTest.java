package at.gepardec.dojo.leistung.au.domain;

import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.au.domain.model.MeldungsErgebnis;
import at.gepardec.dojo.leistung.au.domain.port.AnspruchPruefungPort;
import at.gepardec.dojo.leistung.au.domain.port.AuMeldungPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuMeldungServiceTest {

    private static final Svnr KURT = new Svnr(TestData.SVNR_KURT);
    private static final Svnr EBERHARD = new Svnr(TestData.SVNR_EBERHARD);
    private static final LocalDate AU_BEGINN = LocalDate.of(2026, 8, 6);

    @Mock
    private AuMeldungPort auMeldungPort;
    @Mock
    private AnspruchPruefungPort anspruchPruefungPort;

    private AuMeldungService auMeldungService;

    @BeforeEach
    void setUp() {
        auMeldungService = new AuMeldungService(auMeldungPort, anspruchPruefungPort);
    }

    @Test
    void meldungBeiAnspruchWirdGespeichert() {
        AuMeldung meldung = new AuMeldung(KURT, AU_BEGINN);
        when(anspruchPruefungPort.hatAnspruch(KURT)).thenReturn(true);

        MeldungsErgebnis ergebnis = auMeldungService.melde(meldung);

        verify(auMeldungPort).speichere(meldung);
        assertThat(ergebnis).isEqualTo(new MeldungsErgebnis.Gespeichert(meldung));
        assertThat(ergebnis.istGespeichert()).isTrue();
    }

    @Test
    void meldungOhneAnspruchWirdNichtGespeichert() {
        AuMeldung meldung = new AuMeldung(EBERHARD, AU_BEGINN);
        when(anspruchPruefungPort.hatAnspruch(EBERHARD)).thenReturn(false);

        MeldungsErgebnis ergebnis = auMeldungService.melde(meldung);

        verify(auMeldungPort, never()).speichere(meldung);
        assertThat(ergebnis).isInstanceOf(MeldungsErgebnis.Abgelehnt.class);
        assertThat(ergebnis.istGespeichert()).isFalse();
    }

    @Test
    void ablehnungNenntDenGrund() {
        AuMeldung meldung = new AuMeldung(EBERHARD, AU_BEGINN);
        when(anspruchPruefungPort.hatAnspruch(EBERHARD)).thenReturn(false);

        MeldungsErgebnis ergebnis = auMeldungService.melde(meldung);

        assertThat(ergebnis).isInstanceOfSatisfying(MeldungsErgebnis.Abgelehnt.class,
                abgelehnt -> assertThat(abgelehnt.grund()).isEqualTo(AuMeldungService.GRUND_KEIN_ANSPRUCH));
    }

    @Test
    void meldungIstPflicht() {
        assertThatNullPointerException().isThrownBy(() -> auMeldungService.melde(null));
    }
}
