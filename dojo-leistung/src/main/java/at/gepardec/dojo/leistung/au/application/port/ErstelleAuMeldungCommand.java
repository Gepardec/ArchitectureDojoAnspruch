package at.gepardec.dojo.leistung.au.application.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;

public record ErstelleAuMeldungCommand(Svnr svnr, LocalDate auBeginn) {
    public ErstelleAuMeldungCommand(Svnr svnr, LocalDate auBeginn) {
        this.svnr = Objects.requireNonNull(svnr);
        this.auBeginn = Objects.requireNonNull(auBeginn);
    }
}
