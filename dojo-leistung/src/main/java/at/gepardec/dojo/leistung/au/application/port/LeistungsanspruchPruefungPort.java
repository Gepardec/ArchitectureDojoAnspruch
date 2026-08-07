package at.gepardec.dojo.leistung.au.application.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

public interface LeistungsanspruchPruefungPort {
    boolean pruefeAnspruch(Svnr svnr);
}
