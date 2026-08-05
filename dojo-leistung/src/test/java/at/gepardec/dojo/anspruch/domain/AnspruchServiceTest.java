package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnspruchServiceTest {
    private AnspruchService anspruchService = new AnspruchService();
    private Anspruch eigenAnspruch = new EigenAnspruch();
    private Anspruch kinderAnspruch = new KindAnspruch();

    @Test
    void testEberhardKeinAnspruch() {
        keinAnspruch(TestData.SVNR_EBERHARD);
    }

    @Test
    void testHatAnspruch() {
        hatAnspruch(TestData.SVNR_KURT);
        hatAnspruch(TestData.SVNR_ANGIE);
    }

    @Test
    void testEigenAnspruch() {
        assertTrue(eigenAnspruch.anspruch(new Svnr(TestData.SVNR_KURT)));
        assertFalse(eigenAnspruch.anspruch(new Svnr(TestData.SVNR_MARIA)));
        assertFalse(eigenAnspruch.anspruch(new Svnr(TestData.SVNR_ANGIE)));
    }

    @Test
    void testKinderAnspruch() {
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_KURT)), "Kurt ist kein Kind");
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_MARIA)), "Maria ist kein Kind");
        assertTrue(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_ANGIE)), "Angie ist Kind von Kurt mit Eigenanspruch");
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_EBERHARD)), "Eberhard ist zu alt");
    }

    private void hatAnspruch(String svnr) {
        assertTrue(anspruchService.hasAnspruch(new Svnr(svnr)));
    }

    private void keinAnspruch(String svnr) {
        assertFalse(anspruchService.hasAnspruch(new Svnr(svnr)));
    }
}