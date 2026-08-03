package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnspruchServiceTest {
    private AnspruchService service = new AnspruchService();

    @Test
    void testEberhardKeinAnspruch() {
        keinAnspruch(TestData.SVNR_EBERHARD);
    }

    private void keinAnspruch(String svnr) {
        assertFalse(service.hasAnspruch(new Svnr(svnr)));
    }
}