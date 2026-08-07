package it.at.gepardec.dojo.leistung.anspruch;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.leistung.anspruch.application.PruefeLeistungsanspruchQueryHandler;
import at.gepardec.dojo.leistung.anspruch.application.port.PruefeLeistungsanspruchUseCase;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.leistung.anspruch.infrastructure.VersicherterAdapter;
import at.gepardec.dojo.personen.PersonenService;
import at.gepardec.dojo.test.TestData;
import at.gepardec.dojo.zeiten.ZeitenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PruefeLeistungsanspruchUseCaseSIT {
    private PruefeLeistungsanspruchUseCase useCase;

    @BeforeEach
    void setUp() {
        ZeitenService zeitenService = new ZeitenService();
        PersonenService personenService = new PersonenService();
        AngehoerigeService angehoerigeService = new AngehoerigeService();
        VersicherterAdapter repository = new VersicherterAdapter(zeitenService, personenService, angehoerigeService);
        useCase = new PruefeLeistungsanspruchQueryHandler(repository);
    }

    @Test
    void testHatAnspruch_Eberhart() {
        // given
        Svnr svnr = new Svnr(TestData.SVNR_EBERHARD);

        // when
        boolean anspruch = useCase.hatLeistungsanspruch(svnr);

        // then
        assertThat(anspruch).isFalse();
    }

    @Test
    void testHatAnspruch_Kurt() {
        // given
        Svnr svnr = new Svnr(TestData.SVNR_KURT);

        // when
        boolean anspruch = useCase.hatLeistungsanspruch(svnr);

        // then
        assertThat(anspruch).isTrue();
    }

    @Test
    void testHatAnspruch_Angie() {
        // given
        Svnr svnr = new Svnr(TestData.SVNR_ANGIE);

        // when
        boolean anspruch = useCase.hatLeistungsanspruch(svnr);

        // then
        assertThat(anspruch).isTrue();
    }
}
