package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.anspruch.ports.DomainPortFactory;
import at.gepardec.dojo.anspruch.ports.ZeitenService;
import at.gepardec.dojo.shared.domain.Svnr;

public class EigenAnspruch implements Anspruch{
    ZeitenService zeitenService = DomainPortFactory.getZeitenService();

    @Override
    public boolean anspruch(Svnr svnr) {
        return ZeitenStrecke.hasOpen(zeitenService.getVersicherungsZeiten(svnr));
    }
}