package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.leistung.anspruch.domain.model.Versicherungszeit;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.personen.PersonenService;
import at.gepardec.dojo.svnr.SvnrValidator;
import at.gepardec.dojo.test.TestData;
import at.gepardec.dojo.zeiten.ZeitenService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prüft die Adapter gegen die echten Umsystem-Attrappen aus {@code dojo-external-domains}.
 */
class AdapterTest {

    private static final Svnr KURT = new Svnr(TestData.SVNR_KURT);
    private static final Svnr ANGIE = new Svnr(TestData.SVNR_ANGIE);
    private static final Svnr MARIA = new Svnr(TestData.SVNR_MARIA);
    private static final Svnr UNBEKANNT = new Svnr(SvnrValidator.generateSvnr("010190"));

    private final ZeitenAdapter zeitenAdapter = new ZeitenAdapter(new ZeitenService());
    private final PersonenAdapter personenAdapter = new PersonenAdapter(new PersonenService());
    private final AngehoerigeAdapter angehoerigeAdapter = new AngehoerigeAdapter(new AngehoerigeService());

    @Test
    void zeitenAdapterBildetAufDenDomaenentypAb() {
        List<Versicherungszeit> zeiten = zeitenAdapter.versicherungszeiten(KURT);

        assertThat(zeiten).hasSize(2);
        assertThat(zeiten).anySatisfy(zeit -> assertThat(zeit.istAktiv()).isTrue());
        assertThat(zeiten).anySatisfy(zeit -> assertThat(zeit.bis()).isEqualTo(LocalDate.of(2022, 7, 31)));
    }

    @Test
    void zeitenAdapterLiefertLeereListeStattNull() {
        assertThat(zeitenAdapter.versicherungszeiten(UNBEKANNT)).isEmpty();
    }

    @Test
    void personenAdapterLiefertDasGeburtsdatum() {
        assertThat(personenAdapter.geburtsdatum(ANGIE)).contains(LocalDate.of(2010, 7, 24));
    }

    /**
     * Der Randfall, an dem beide bestehenden Lösungen mit einer NullPointerException abbrechen:
     * {@code PersonenService.getPerson} liefert für unbekannte Nummern {@code null}.
     */
    @Test
    void personenAdapterLiefertLeeresOptionalStattNull() {
        assertThat(personenAdapter.geburtsdatum(UNBEKANNT)).isEqualTo(Optional.empty());
    }

    @Test
    void angehoerigeAdapterLiefertNurElternteile() {
        List<Svnr> eltern = angehoerigeAdapter.eltern(ANGIE);

        assertThat(eltern).containsExactlyInAnyOrder(MARIA, KURT);
    }

    @Test
    void angehoerigeAdapterFiltertEhepartnerUndKinderWeg() {
        // Kurt hat eine Ehepartner- und zwei Kindbeziehungen, aber keinen Elternteil.
        assertThat(angehoerigeAdapter.eltern(KURT)).isEmpty();
    }

    @Test
    void angehoerigeAdapterLiefertLeereListeStattNull() {
        assertThat(angehoerigeAdapter.eltern(UNBEKANNT)).isEmpty();
    }
}
