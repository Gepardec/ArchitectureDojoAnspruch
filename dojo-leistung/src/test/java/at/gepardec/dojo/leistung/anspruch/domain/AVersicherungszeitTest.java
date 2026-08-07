package at.gepardec.dojo.leistung.anspruch.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AVersicherungszeitTest {
    @Test
    void testConstructor() {
        // given

        // when
        AVersicherungszeit zeit = new AVersicherungszeit(LocalDate.of(2026, 1, 1), null);

        // then
        assertThat(zeit).isNotNull();
    }

    @Test
    void testConstructor_vonNull() {
        // given

        // when
        NullPointerException npe = catchNullPointerException(() -> new AVersicherungszeit(null, null));

        // then
        assertThat(npe).isNotNull();
    }

    @Test
    void testConstructor_bisNotNull() {
        // given

        // when
        AVersicherungszeit zeit = new AVersicherungszeit(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));

        // then
        assertThat(zeit).isNotNull();
    }

    @Test
    void testConstructor_bisBeforeVon() {
        // given

        // when
        IllegalArgumentException ex = catchIllegalArgumentException(() -> new AVersicherungszeit(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 1)));

        // then
        assertThat(ex).isNotNull();
    }

    @Test
    void testContains() {
        // given
        AVersicherungszeit zeit = new AVersicherungszeit(LocalDate.of(2026, 1, 1), null);

        // when - then
        assertThat(zeit.contains(LocalDate.of(2026, 1, 5))).isTrue();
        assertThat(zeit.contains(LocalDate.of(2026, 1, 1))).isTrue();
        assertThat(zeit.contains(LocalDate.of(2222, 1, 5))).isTrue();
        assertThat(zeit.contains(LocalDate.of(2000, 1, 5))).isFalse();
        assertThat(zeit.contains(LocalDate.of(1980, 1, 5))).isFalse();
    }

    @Test
    void testContains_null() {
        // given
        AVersicherungszeit zeit = new AVersicherungszeit(LocalDate.of(2026, 1, 1), null);

        // when
        NullPointerException npe = catchNullPointerException(() -> zeit.contains(null));

        // then
        assertThat(npe).isNotNull();
    }
}