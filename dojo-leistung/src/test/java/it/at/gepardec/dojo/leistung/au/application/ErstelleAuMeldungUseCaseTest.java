package it.at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.leistung.au.application.ErstelleAuMeldungCommandHandler;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungCommand;
import at.gepardec.dojo.leistung.au.application.port.ErstelleAuMeldungUseCase;
import at.gepardec.dojo.leistung.au.infrastructure.JpaAuMeldungRepository;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ErstelleAuMeldungUseCaseTest {
    private ErstelleAuMeldungUseCase useCase;

    @BeforeEach
    void setUp() {
        JpaAuMeldungRepository repository = new JpaAuMeldungRepository();
        useCase = new ErstelleAuMeldungCommandHandler(repository);
    }

    @Test
    void testErstelleAuMeldung_Kurt() {
        // given
        ErstelleAuMeldungCommand command = new ErstelleAuMeldungCommand(new Svnr(TestData.SVNR_KURT), LocalDate.of(2026,8,6));

        // when
        useCase.erstelleAuMeldung(command);

        // then
    }
}