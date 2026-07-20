package at.gepardec.dojo.personen;

import java.time.LocalDate;

public record Person(String vorname, String nachname, LocalDate geburtsDatum) {
}
