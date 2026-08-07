package at.gepardec.dojo.leistung.anspruch.application.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.leistung.anspruch.domain.Versicherter;

public interface VersicherterRepository {
    Versicherter findBySvnr(Svnr svnr);
}
