package at.gepardec.dojo.leistung.au.infrastructure;

import at.gepardec.dojo.leistung.anspruch.application.PruefeAnspruchUseCase;
import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AuMeldungAdapterTest {

    private static final AuMeldung MELDUNG =
            new AuMeldung(new Svnr(TestData.SVNR_KURT), LocalDate.of(2026, 8, 6));

    @Test
    void gespeicherteMeldungIstAbrufbar() {
        AuMeldungAdapter adapter = new AuMeldungAdapter();

        adapter.speichere(MELDUNG);

        assertThat(adapter.gespeicherte()).containsExactly(MELDUNG);
    }

    @Test
    void adapterStartetLeer() {
        assertThat(new AuMeldungAdapter().gespeicherte()).isEmpty();
    }

    @Test
    void anspruchPruefungAdapterReichtAnDenFremdenKontextWeiter() {
        Svnr[] empfangen = new Svnr[1];
        PruefeAnspruchUseCase fremderKontext = svnr -> {
            empfangen[0] = svnr;
            return true;
        };
        AnspruchPruefungAdapter adapter = new AnspruchPruefungAdapter(fremderKontext);

        boolean ergebnis = adapter.hatAnspruch(new Svnr(TestData.SVNR_KURT));

        assertThat(ergebnis).isTrue();
        assertThat(empfangen[0]).isEqualTo(new Svnr(TestData.SVNR_KURT));
    }
}
