package at.gepardec.dojo.au.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AUService {
    private static final Logger log = LoggerFactory.getLogger(AUService.class);

    public void anlegen(Meldung meldung) {
        log.info("Meldung wurde gespeichert: {}", meldung);
    }
}
