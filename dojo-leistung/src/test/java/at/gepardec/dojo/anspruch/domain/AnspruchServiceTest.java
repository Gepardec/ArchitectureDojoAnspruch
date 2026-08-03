package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnspruchServiceTest {
    private AnspruchService service = new AnspruchService();
    private Anspruch eigenAnspruch = new EigenAnspruch();

    @Test
    void testEberhardKeinAnspruch() {
        keinAnspruch(TestData.SVNR_EBERHARD);
    }

    @Test
    void testKurtHatAnspruch() {
        hatAnspruch(TestData.SVNR_KURT);
    }

    @Test
    void testEigenAnspruch() {
        assertTrue(eigenAnspruch.anspruch(new Svnr(TestData.SVNR_KURT)));
        assertFalse(eigenAnspruch.anspruch(new Svnr(TestData.SVNR_MARIA)));
        assertFalse(eigenAnspruch.anspruch(new Svnr(TestData.SVNR_ANGIE)));
    }

    private void hatAnspruch(String svnr) {
        assertTrue(service.hasAnspruch(new Svnr(svnr)));
    }

    private void keinAnspruch(String svnr) {
        assertFalse(service.hasAnspruch(new Svnr(svnr)));
    }
}