package at.gepardec.dojo.zeiten;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ZeitenTest {

    private ZeitenService zsvc = new ZeitenService();

    @Test
    public void testKeineZeitenGivesEmpty(){
        List<VersicherungsZeit> zeiten = zsvc.getVersicherungsZeiten(TestData.SVNR_EBERHARD);
        assertTrue(zeiten.isEmpty(), "Versicherungszeiten für Eberhard sind leer.");
    }

    @Test
    public void testKurtHatZeiten() {
        List<VersicherungsZeit> zeiten = zsvc.getVersicherungsZeiten(TestData.SVNR_KURT);
        assertFalse(zeiten.isEmpty(), "Versicherungszeiten für Kurt sind nicht leer.");
    }

    @Test
    void testKurtHatAktiveVersicherungszeit() {
        assertTrue(hatAktiveVersicherungszeit(TestData.SVNR_KURT), "Kurt hat aktive Versicherungszeit");
    }

    private boolean hatAktiveVersicherungszeit(String svnr) {
        boolean hatAktive = false;
        for (VersicherungsZeit versicherungsZeit: zsvc.getVersicherungsZeiten(svnr)){
            if ( versicherungsZeit.bis() == null){
                hatAktive = true;
            }
        }
        return hatAktive;
    }
}
