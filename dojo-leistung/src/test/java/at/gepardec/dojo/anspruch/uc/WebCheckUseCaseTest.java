package at.gepardec.dojo.anspruch.uc;

import at.gepardec.dojo.application.Application;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WebCheckUseCaseTest {

    private AnspruchWebCheck anspruch;

    @BeforeAll
    void setUp() {
        Application.init();
        anspruch = new AnspruchWebCheck();
    }

    @Test
    void testEberhardKeinAnspruch() {
        assertEquals("Kein Anspruch", anspruch.check(TestData.SVNR_EBERHARD));
    }

    @Test
    void testKurtHatAnspruch() {
        assertEquals("Anspruch vorhanden", anspruch.check(TestData.SVNR_KURT));
    }

}
