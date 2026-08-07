package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.domain.port.PersonenPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.personen.Person;
import at.gepardec.dojo.personen.PersonenService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Bindet die Personendaten (Applikation ZPV) an.
 * <p>
 * {@code PersonenService.getPerson} liefert für unbekannte Nummern {@code null}. Genau dieses
 * {@code null} wird hier in ein {@link Optional} überführt, statt es weiterzureichen -- in beiden
 * bestehenden Lösungen führt das durchgereichte {@code null} zu einer NullPointerException.
 */
public class PersonenAdapter implements PersonenPort {

    private final PersonenService personenService;

    public PersonenAdapter(PersonenService personenService) {
        this.personenService = Objects.requireNonNull(personenService, "personenService");
    }

    @Override
    public Optional<LocalDate> geburtsdatum(Svnr svnr) {
        Objects.requireNonNull(svnr, "svnr");

        Person person = personenService.getPerson(svnr.nummer());
        return Optional.ofNullable(person).map(Person::geburtsDatum);
    }
}
