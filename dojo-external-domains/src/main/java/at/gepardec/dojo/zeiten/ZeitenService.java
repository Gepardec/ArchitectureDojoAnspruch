package at.gepardec.dojo.zeiten;

import at.gepardec.dojo.log.Performance;
import at.gepardec.dojo.test.TestData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Das ZeitenService liefert Versicherungszeiten aus der Applikation MVB.
 * Wenn das Bis-Datum einer Versicherungszeit nicht gesetzt ist, dann ist die Zeit aktiv
 */
public class ZeitenService {
    public List<VersicherungsZeit> getVersicherungsZeiten(String svnr) {

        Performance.logExternalCall("getVersicherungsZeiten", svnr);
        switch (svnr){
            case TestData.SVNR_KURT -> {
                return zeitenKurt();
            }
        }

        return new ArrayList<VersicherungsZeit>();
    }

    private List<VersicherungsZeit> zeitenKurt() {
        ArrayList<VersicherungsZeit> zeiten = new ArrayList<VersicherungsZeit>();
        zeiten.add(new VersicherungsZeit(LocalDate.of(2013, 3, 1), LocalDate.of(2022, 7, 31)));
        zeiten.add(new VersicherungsZeit(LocalDate.of(2022, 9, 1), null));
        return zeiten;
    }
}
