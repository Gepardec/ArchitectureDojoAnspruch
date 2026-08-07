package at.gepardec.dojo.leistung.au.application.port;

import at.gepardec.dojo.leistung.au.domain.AuMeldung;

public interface AuMeldungRepository {
    void speichere(AuMeldung meldung);
}
