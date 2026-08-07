package at.gepardec.dojo.leistung.au.application;

import at.gepardec.dojo.leistung.au.domain.model.MeldungsErgebnis;

import java.time.LocalDate;

/**
 * Einstiegspunkt in den AU-Kontext (Input Port). Getrieben wird er von der Messaging-Anbindung
 * an die Arztsoftware.
 */
public interface ErstelleAuMeldungUseCase {

    MeldungsErgebnis erstelleAuMeldung(String svnr, LocalDate auBeginn);
}
