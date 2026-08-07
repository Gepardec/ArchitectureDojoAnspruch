package at.gepardec.dojo.leistung.anspruch.domain;

import java.time.LocalDate;
import java.util.Objects;

public record AVersicherungszeit(LocalDate von, LocalDate bis) {
    public AVersicherungszeit(LocalDate von, LocalDate bis) {
        this.von = Objects.requireNonNull(von);

        if (bis != null && bis.isBefore(von)) throw new IllegalArgumentException("bis muss nach von liegen");

        this.bis = bis;
    }

    public boolean contains(LocalDate date) {
        Objects.requireNonNull(date);
        return (von.isEqual(date) || von.isBefore(date))
                && (bis == null || !date.isAfter(bis));
    }
}
