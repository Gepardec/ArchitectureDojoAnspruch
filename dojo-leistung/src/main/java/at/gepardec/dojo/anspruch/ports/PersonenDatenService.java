package at.gepardec.dojo.anspruch.ports;

import at.gepardec.dojo.anspruch.domain.Svnr;

import java.time.LocalDate;
import java.util.List;

public interface PersonenDatenService {
    LocalDate getGeburtsDatum(Svnr svnr);

    List<Svnr> getEltern(Svnr svnr);
}
