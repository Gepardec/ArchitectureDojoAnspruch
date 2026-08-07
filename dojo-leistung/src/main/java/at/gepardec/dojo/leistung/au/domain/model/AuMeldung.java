package at.gepardec.dojo.leistung.au.domain.model;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Elektronische Arbeitsunfähigkeitsmeldung: wer ist ab wann krankgeschrieben.
 */
public record AuMeldung(Svnr svnr, LocalDate auBeginn) {

    public AuMeldung {
        Objects.requireNonNull(svnr, "svnr");
        Objects.requireNonNull(auBeginn, "auBeginn");
    }
}
