package at.gepardec.dojo.au.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AUServiceTest {
    private AUService meldungService = new AUService();
    @Test
    void testKurtMeldungWirdGespeichert() {
        Meldung meldung = new Meldung(LocalDate.now(), TestData.SVNR_KURT);

        meldungService.anlegen(meldung);
    }

    @Test
    void testEberhardMeldungWirdNichtGespeichert() {
        Meldung meldung = new Meldung(LocalDate.now(), TestData.SVNR_EBERHARD);

        boolean exception = false;
        try {
            meldungService.anlegen(meldung);
        } catch (Exception e) {
            exception = true;
        }
        assertTrue(exception, "Exception must be thrown");
    }

}
