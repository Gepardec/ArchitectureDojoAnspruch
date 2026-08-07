package at.gepardec.dojo.leistung.anspruch.domain;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Versicherter {
    private final Svnr svnr;
    private final LocalDate geburtsdatum;
    private final List<AVersicherungszeit> versicherungszeiten;
    private List<Versicherter> eltern = new ArrayList<>();

    private Versicherter(Svnr svnr, LocalDate geburtsdatum, List<AVersicherungszeit> versicherungszeiten) {
        this.svnr = Objects.requireNonNull(svnr);
        this.geburtsdatum = geburtsdatum;
        this.versicherungszeiten = new ArrayList<>(Objects.requireNonNull(versicherungszeiten));
    }

    public static Versicherter fromVersicherungszeiten(Svnr svnr, LocalDate geburtsdatum, List<AVersicherungszeit> aVersicherungszeiten) {
        return new Versicherter(svnr, geburtsdatum, aVersicherungszeiten);
    }

    public boolean isVersichert(LocalDate date) {
        Objects.requireNonNull(date);
        boolean eigenversichert = isEigenversichert(date);
        if(eigenversichert) return true;

        if(isUnter18()) {
            return isMitElternMitversichert(date);
        }

        return false;
    }

    private boolean isEigenversichert(LocalDate date) {
        return versicherungszeiten.stream().anyMatch(v -> v.contains(date));
    }

    private boolean isMitElternMitversichert(LocalDate date) {
        return eltern.stream().anyMatch(e -> e.isVersichert(date));
    }

    public void addElternteil(Versicherter versicherter) {
        Objects.requireNonNull(versicherter);
        eltern.add(versicherter);
    }

    private boolean isUnter18(){
        return geburtsdatum.plusYears(18).isAfter(LocalDate.now());
    }
}
