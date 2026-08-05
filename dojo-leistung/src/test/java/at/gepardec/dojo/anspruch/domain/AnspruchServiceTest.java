package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.anspruch.ports.DomainPortFactory;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AnspruchServiceTest {
    @BeforeEach
    void setUp() {
        DomainPortFactory.getSystemDaten().setToday(LocalDate.of(2026, 8, 5));
    }

    @AfterEach
    void tearDown() {
        DomainPortFactory.getSystemDaten().resetToday();
    }

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
    void testKindAnspruch() {
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_KURT)), "Kurt ist kein Kind");
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_MARIA)), "Maria ist kein Kind");
        assertTrue(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_ANGIE)), "Angie ist Kind von Kurt mit Eigenanspruch");
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_EBERHARD)), "Eberhard ist zu alt");
    }

    @Test
    void testZukunftAnspruch() {
        DomainPortFactory.getSystemDaten().setToday(LocalDate.of(2030, 8, 5));
        assertFalse(kinderAnspruch.anspruch(new Svnr(TestData.SVNR_ANGIE)), "2030 ist Angie ist zu alt");
    }

    private void hatAnspruch(String svnr) {
        assertTrue(anspruchService.hasAnspruch(new Svnr(svnr)));
    }

    private void keinAnspruch(String svnr) {
        assertFalse(anspruchService.hasAnspruch(new Svnr(svnr)));
    }
}