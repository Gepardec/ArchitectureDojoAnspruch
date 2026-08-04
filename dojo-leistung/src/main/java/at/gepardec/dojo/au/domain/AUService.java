package at.gepardec.dojo.au.domain;

import at.gepardec.dojo.anspruch.domain.AnspruchService;
import at.gepardec.dojo.anspruch.domain.Svnr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AUService {
    private static final Logger log = LoggerFactory.getLogger(AUService.class);

    public void anlegen(Meldung meldung) {
        AnspruchService anspruchService = new AnspruchService();
        if (anspruchService.hasAnspruch(new Svnr(meldung.svnr()))) {
            log.info("Meldung wurde gespeichert: {}", meldung);
        }
        else{
            log.info("Meldung wurde nicht gespeichert, weil kein Anspruch: {}", meldung);
            throw new KeinAnspruchException(meldung.svnr());
        }
    }
}
