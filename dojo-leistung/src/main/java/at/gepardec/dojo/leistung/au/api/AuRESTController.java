package at.gepardec.dojo.leistung.au.api;

import at.gepardec.dojo.leistung.anspruch.api.Response;
import at.gepardec.dojo.leistung.au.domain.AuMeldung;

import java.time.LocalDate;

public interface AuRESTController {
    void erstelleAuMeldung(String svnr, LocalDate auBeginn);
}
