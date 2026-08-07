package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.angehoerige.AngehoerigenBeziehung;
import at.gepardec.dojo.leistung.anspruch.application.port.VersicherterRepository;
import at.gepardec.dojo.leistung.anspruch.domain.AVersicherungszeit;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.leistung.anspruch.domain.Versicherter;
import at.gepardec.dojo.personen.Person;
import at.gepardec.dojo.personen.PersonenService;
import at.gepardec.dojo.zeiten.VersicherungsZeit;
import at.gepardec.dojo.zeiten.ZeitenService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class VersicherterAdapter implements VersicherterRepository {
    private final ZeitenService zeitenService;
    private final PersonenService personenService;
    private final AngehoerigeService angehoerigeService;

    public VersicherterAdapter(ZeitenService zeitenService, PersonenService personenService, AngehoerigeService angehoerigeService) {
        this.zeitenService = zeitenService;
        this.personenService = personenService;
        this.angehoerigeService = angehoerigeService;
    }

    @Override
    public Versicherter findBySvnr(Svnr svnr) {
        Versicherter versicherter = leseVersicherter(svnr);
        List<Versicherter> eltern = leseEltern(svnr);
        eltern.forEach(versicherter::addElternteil);
        return versicherter;
    }

    private List<Versicherter> leseEltern(Svnr svnr) {
        List<AngehoerigenBeziehung> beziehungen = angehoerigeService.getAngehoerigenBeziehung(svnr.nummer());
        return beziehungen.stream()
                .filter(beziehung -> beziehung.angehoerigerTyp().equals(AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL))
                .map(beziehung -> leseVersicherter(new Svnr(beziehung.angehoerigerVsnr())))
                .toList();
    }

    private Versicherter leseVersicherter(Svnr svnr) {
        Objects.requireNonNull(svnr);

        LocalDate geburtsdatum = getGeburtsdatum(svnr);
        List<AVersicherungszeit> aVersicherungszeiten = getVersicherungszeiten(svnr);
        return Versicherter.fromVersicherungszeiten(svnr, geburtsdatum, aVersicherungszeiten);
    }

    private LocalDate getGeburtsdatum(Svnr svnr) {
        Person person = personenService.getPerson(svnr.nummer());
        if (person == null) return null;

        return person.geburtsDatum();
    }

    private List<AVersicherungszeit> getVersicherungszeiten(Svnr svnr) {
        List<VersicherungsZeit> zeiten = zeitenService.getVersicherungsZeiten(svnr.nummer());
        return zeiten.stream().map(vz -> new AVersicherungszeit(vz.von(), vz.bis())).toList();
    }
}
