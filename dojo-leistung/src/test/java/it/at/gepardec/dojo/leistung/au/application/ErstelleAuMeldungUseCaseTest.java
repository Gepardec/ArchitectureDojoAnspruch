package it.at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.leistung.anspruch.application.PruefeLeistungsanspruchQueryHandler;
import at.gepardec.dojo.leistung.anspruch.infrastructure.VersicherterAdapter;
import at.gepardec.dojo.leistung.au.application.ErstelleAuMeldungCommandHandler;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungCommand;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungUseCase;
import at.gepardec.dojo.leistung.au.infrastructure.JpaAuMeldungRepository;
import at.gepardec.dojo.leistung.au.infrastructure.LeistungsanspruchPruefungAdapter;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.personen.PersonenService;
import at.gepardec.dojo.test.TestData;
import at.gepardec.dojo.zeiten.ZeitenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ErstelleAuMeldungUseCaseTest {
    private ErstelleAuMeldungUseCase useCase;

    @BeforeEach
    void setUp() {
        JpaAuMeldungRepository repository = new JpaAuMeldungRepository();
        VersicherterAdapter versicherterAdapter = new VersicherterAdapter(new ZeitenService(), new PersonenService(), new AngehoerigeService());
        PruefeLeistungsanspruchQueryHandler queryHandler = new PruefeLeistungsanspruchQueryHandler(versicherterAdapter);
        LeistungsanspruchPruefungAdapter anspruchPruefungPort = new LeistungsanspruchPruefungAdapter(queryHandler);
        useCase = new ErstelleAuMeldungCommandHandler(repository, anspruchPruefungPort);
    }

    @Test
    void testErstelleAuMeldung_Kurt() {
        // given
        ErstelleAuMeldungCommand command = new ErstelleAuMeldungCommand(new Svnr(TestData.SVNR_KURT), LocalDate.of(2026,8,6));

        // when
        useCase.erstelleAuMeldung(command);

        // then
    }

    @Test
    void testErstelleAuMeldung_Eberhart() {
        // given
        ErstelleAuMeldungCommand command = new ErstelleAuMeldungCommand(new Svnr(TestData.SVNR_EBERHARD), LocalDate.of(2026,8,6));

        // when
        useCase.erstelleAuMeldung(command);

        // then
    }
}