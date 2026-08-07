package at.gepardec.dojo.leistung.anspruch.domain;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SvnrTest {
    @Test
    void testConstructor() {
        // given

        // when
        Svnr svnr = new Svnr(TestData.SVNR_EBERHARD);

        // then
        assertThat(svnr).isNotNull();
    }
}