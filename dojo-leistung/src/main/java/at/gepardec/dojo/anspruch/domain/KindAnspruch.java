package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.anspruch.ports.DomainPortFactory;
import at.gepardec.dojo.anspruch.ports.PersonenDatenService;

import java.time.LocalDate;
import java.time.Period;

public class KindAnspruch implements Anspruch {
    private PersonenDatenService personenDatenService = DomainPortFactory.getPersonenDatenService();
    private Anspruch eigenAnspruch = new EigenAnspruch();

    @Override
    public boolean anspruch(Svnr kind) {

        if (alter(personenDatenService.getGeburtsDatum(kind)) > 18) {
            return false;
        }
        for (Svnr elternteil : personenDatenService.getEltern(kind)) {
            if ( eigenAnspruch.anspruch(elternteil) ) {
                return true;
            }
        }
        return false;
    }

    private int alter(LocalDate geburtsDatum) {
        return Period.between(geburtsDatum, LocalDate.now()).getYears();
    }
}

