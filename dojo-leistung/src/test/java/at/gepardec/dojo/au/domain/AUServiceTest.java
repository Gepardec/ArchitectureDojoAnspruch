package at.gepardec.dojo.au.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class AUServiceTest {

    @Test
    void testKurtMeldungWirdGespeichert() {
        Meldung meldung = new Meldung(LocalDate.now(), TestData.SVNR_KURT);

        AUService meldungService = new AUService();
        meldungService.anlegen(meldung);
    }
}
