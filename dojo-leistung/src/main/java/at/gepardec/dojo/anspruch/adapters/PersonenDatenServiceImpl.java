package at.gepardec.dojo.anspruch.adapters;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.angehoerige.AngehoerigenBeziehung;
import at.gepardec.dojo.shared.domain.Svnr;
import at.gepardec.dojo.anspruch.ports.PersonenDatenService;
import at.gepardec.dojo.personen.PersonenService;

import java.time.LocalDate;
import java.util.List;

public class PersonenDatenServiceImpl implements PersonenDatenService {
    private PersonenService personenService = new PersonenService();
    private AngehoerigeService angehoerigeService = new AngehoerigeService();

    @Override
    public LocalDate getGeburtsDatum(Svnr svnr) {
        return personenService.getPerson(svnr.asString()).geburtsDatum();
    }

    @Override
    public List<Svnr> getEltern(Svnr svnr) {
        List<AngehoerigenBeziehung> bez = angehoerigeService.getAngehoerigenBeziehung(svnr.asString());
        return bez.stream().filter(
                b -> b.angehoerigerTyp().equals(AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL))
                .map( b -> new Svnr(b.angehoerigerVsnr())).toList();
    }
}
