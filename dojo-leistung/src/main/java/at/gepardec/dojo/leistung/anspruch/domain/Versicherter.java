package at.gepardec.dojo.leistung.anspruch.domain;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Versicherter {
    private final Svnr svnr;
    private final List<AVersicherungszeit> versicherungszeiten;

    private Versicherter(Svnr svnr, List<AVersicherungszeit> versicherungszeiten) {
        this.svnr = Objects.requireNonNull(svnr);
        this.versicherungszeiten = new ArrayList<>(Objects.requireNonNull(versicherungszeiten));
    }

    public static Versicherter fromVersicherungszeiten(Svnr svnr, List<AVersicherungszeit> aVersicherungszeiten) {
        return new Versicherter(svnr, aVersicherungszeiten);
    }

    public boolean isVersichert(LocalDate date) {
        Objects.requireNonNull(date);
        return versicherungszeiten.stream().anyMatch(v -> v.contains(date));
    }
}
