package at.gepardec.dojo.leistung.anspruch.application;

import at.gepardec.dojo.leistung.anspruch.application.port.PruefeLeistungsanspruchUseCase;
import at.gepardec.dojo.leistung.anspruch.domain.Svnr;

public class PruefeLeistungsanspruchQueryHandler implements PruefeLeistungsanspruchUseCase {
    @Override
    public boolean hatLeistungsanspruch(Svnr svnr) {
        return false;
    }
}
