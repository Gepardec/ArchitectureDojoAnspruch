package at.gepardec.dojo.leistung.au.domain.port;

import at.gepardec.dojo.leistung.au.domain.model.AuMeldung;

/**
 * Ablage der AU-Meldungen. Liegt im Domänenring des AU-Kontexts (Onion).
 */
public interface AuMeldungPort {

    void speichere(AuMeldung meldung);
}
