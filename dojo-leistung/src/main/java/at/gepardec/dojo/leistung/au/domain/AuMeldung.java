package at.gepardec.dojo.leistung.au.domain;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.Objects;

public class AuMeldung {
    private final Svnr svnr;
    private final LocalDate auBeginn;

    public AuMeldung(Svnr svnr, LocalDate auBeginn) {
        this.svnr = Objects.requireNonNull(svnr);
        this.auBeginn = Objects.requireNonNull(auBeginn);
    }

    @Override
    public String toString() {
        return "AuMeldung{" +
                "svnr=" + svnr +
                ", auBeginn=" + auBeginn +
                '}';
    }
}
