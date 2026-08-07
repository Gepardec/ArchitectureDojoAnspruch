package at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.leistung.au.domain.AuMeldungService;
import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;
import at.gepardec.dojo.leistung.au.domain.model.MeldungsErgebnis;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Übersetzt die eingehenden Rohdaten in das Domänenmodell und übergibt. Enthält bewusst keine
 * Fachregel -- die Entscheidung, ob gespeichert werden darf, liegt in {@link AuMeldungService}.
 */
public class ErstelleAuMeldungService implements ErstelleAuMeldungUseCase {

    private final AuMeldungService auMeldungService;

    public ErstelleAuMeldungService(AuMeldungService auMeldungService) {
        this.auMeldungService = Objects.requireNonNull(auMeldungService, "auMeldungService");
    }

    @Override
    public MeldungsErgebnis erstelleAuMeldung(String svnr, LocalDate auBeginn) {
        AuMeldung meldung = new AuMeldung(new Svnr(svnr), auBeginn);

        return auMeldungService.melde(meldung);
    }
}
