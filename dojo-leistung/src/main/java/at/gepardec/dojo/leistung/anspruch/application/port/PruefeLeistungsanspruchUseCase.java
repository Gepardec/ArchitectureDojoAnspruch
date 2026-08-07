package at.gepardec.dojo.leistung.anspruch.application.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

public interface PruefeLeistungsanspruchUseCase {
    boolean hatLeistungsanspruch(Svnr svnr);
}
