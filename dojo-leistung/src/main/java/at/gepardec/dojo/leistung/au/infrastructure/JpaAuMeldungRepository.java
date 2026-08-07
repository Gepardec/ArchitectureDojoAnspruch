package at.gepardec.dojo.leistung.au.infrastructure;

import at.gepardec.dojo.leistung.au.application.port.AuMeldungRepository;
import at.gepardec.dojo.leistung.au.domain.AuMeldung;
import at.gepardec.dojo.svnr.SvnrValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaAuMeldungRepository implements AuMeldungRepository {
    private static final Logger log = LoggerFactory.getLogger(JpaAuMeldungRepository.class);

    @Override
    public void speichere(AuMeldung meldung) {
        log.info("Speichere AuMeldung: {}", meldung);
    }
}
