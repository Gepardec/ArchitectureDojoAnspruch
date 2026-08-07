package at.gepardec.dojo.leistung.anspruch.domain;

import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VersicherterTest {
    @Test
    void testFromVersicherungszeiten() {
        // given

        // when
        Versicherter versicherter = Versicherter.fromVersicherungszeiten(new Svnr(TestData.SVNR_EBERHARD), List.of());

        // then
        assertThat(versicherter).isNotNull();
    }

    @Test
    void testFromVersicherungszeiten_vzNull() {
        // given

        // when

        // then
    }

    @Test
    void testFromVersicherungszeiten_svnrNull() {
        // given

        // when

        // then
    }

    @Test
    void testIsVersichert_nullDate() {
        // given

        // when

        // then
    }

    @Test
    void testIsVersichert_notContainedDate() {
        // given

        // when

        // then
    }

    @Test
    void testIsVersichert_containedDate() {
        // given

        // when

        // then
    }
}