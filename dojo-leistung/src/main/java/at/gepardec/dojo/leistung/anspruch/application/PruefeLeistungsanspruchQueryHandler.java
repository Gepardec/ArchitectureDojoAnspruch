package at.gepardec.dojo.leistung.anspruch.application;

import at.gepardec.dojo.leistung.anspruch.application.port.PruefeLeistungsanspruchUseCase;
import at.gepardec.dojo.leistung.anspruch.application.port.VersicherterRepository;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.leistung.anspruch.domain.Versicherter;

import java.time.LocalDate;
import java.util.Objects;

public class PruefeLeistungsanspruchQueryHandler implements PruefeLeistungsanspruchUseCase {
    private final VersicherterRepository repository;

    public PruefeLeistungsanspruchQueryHandler(VersicherterRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean hatLeistungsanspruch(Svnr svnr) {
        Objects.requireNonNull(svnr);

        Versicherter versicherter = repository.findBySvnr(svnr);
        if(versicherter == null) return false;

        return versicherter.isVersichert(LocalDate.now());
    }
}
