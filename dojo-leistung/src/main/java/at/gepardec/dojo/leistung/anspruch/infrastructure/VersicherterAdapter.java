package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.application.port.VersicherterRepository;
import at.gepardec.dojo.leistung.anspruch.domain.AVersicherungszeit;
import at.gepardec.dojo.leistung.anspruch.domain.Svnr;
import at.gepardec.dojo.leistung.anspruch.domain.Versicherter;
import at.gepardec.dojo.zeiten.VersicherungsZeit;
import at.gepardec.dojo.zeiten.ZeitenService;

import java.util.List;
import java.util.Objects;

public class VersicherterAdapter implements VersicherterRepository {
    private ZeitenService zeitenService;

    public VersicherterAdapter(ZeitenService zeitenService) {
        this.zeitenService = zeitenService;
    }

    @Override
    public Versicherter getBySvnr(Svnr svnr) {
        Objects.requireNonNull(svnr);

        List<VersicherungsZeit> zeiten = zeitenService.getVersicherungsZeiten(svnr.nummer());
        List<AVersicherungszeit> aVersicherungszeiten = zeiten.stream().map(vz -> new AVersicherungszeit(vz.von(), vz.bis())).toList();
        return Versicherter.fromVersicherungszeiten(svnr, aVersicherungszeiten);
    }
}
