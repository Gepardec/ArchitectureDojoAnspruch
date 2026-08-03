package at.gepardec.dojo.anspruch.uc;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WebCheckUseCaseTest {
    private AnspruchWebCheck anspruch = new AnspruchWebCheck();

    @Test
    void testEberhardKeinAnspruch() {
        assertEquals("Kein Anspruch", anspruch.check(TestData.SVNR_EBERHARD));
    }
}
