package at.gepardec.dojo.anspruch.domain;

import at.gepardec.dojo.anspruch.ports.AnspruchRegeln;
import at.gepardec.dojo.anspruch.ports.DomainPortFactory;
import at.gepardec.dojo.anspruch.ports.PersonenDatenService;
import at.gepardec.dojo.anspruch.ports.SystemDaten;
import at.gepardec.dojo.shared.domain.Svnr;

import java.time.LocalDate;
import java.time.Period;

public class KindAnspruch implements Anspruch {
    private PersonenDatenService personenDatenService = DomainPortFactory.getPersonenDatenService();
    private AnspruchRegeln anspruchRegeln = DomainPortFactory.getAnspruchRegeln();
    private SystemDaten systemDaten = DomainPortFactory.getSystemDaten();
    private Anspruch eigenAnspruch = new EigenAnspruch();

    @Override
    public boolean anspruch(Svnr svnr) {

        int altersGrenzeKind = anspruchRegeln.getAltersgrenzeKind();
        if (alter(personenDatenService.getGeburtsDatum(svnr)) > altersGrenzeKind) {
            return false;
        }
        for (Svnr elternteil : personenDatenService.getEltern(svnr)) {
            if ( eigenAnspruch.anspruch(elternteil) ) {
                return true;
            }
        }
        return false;
    }

    private int alter(LocalDate geburtsDatum) {
        return Period.between(geburtsDatum, systemDaten.today()).getYears();
    }
}

