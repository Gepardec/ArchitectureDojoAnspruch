package at.gepardec.dojo.angehoerige;

import at.gepardec.dojo.test.TestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AngehoerigeService {

    public List<AngehoerigenBeziehung> getAngehoerigenBeziehung(String svnr) {
        switch (svnr) {
            case TestData.SVNR_ANGIE -> {
                ArrayList<AngehoerigenBeziehung> eltern = new ArrayList<>();
                eltern.add(new AngehoerigenBeziehung(TestData.SVNR_ANGIE, TestData.SVNR_MARIA, AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL));
                eltern.add(new AngehoerigenBeziehung(TestData.SVNR_ANGIE, TestData.SVNR_KURT, AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL));
                return eltern;
            }
            case TestData.SVNR_MARIA -> {
                ArrayList<AngehoerigenBeziehung> beziehungen = new ArrayList<>();
                beziehungen.add(new AngehoerigenBeziehung(TestData.SVNR_MARIA, TestData.SVNR_KURT, AngehoerigenBeziehung.ANG_TYP_EHEPARTNER));
                beziehungen.add(new AngehoerigenBeziehung(TestData.SVNR_MARIA, TestData.SVNR_ANGIE, AngehoerigenBeziehung.ANG_TYP_KIND));
                beziehungen.add(new AngehoerigenBeziehung(TestData.SVNR_MARIA, TestData.SVNR_EBERHARD, AngehoerigenBeziehung.ANG_TYP_KIND));
                return beziehungen;
            }
            case TestData.SVNR_EBERHARD -> {
                ArrayList<AngehoerigenBeziehung> eltern = new ArrayList<>();
                eltern.add(new AngehoerigenBeziehung(TestData.SVNR_EBERHARD, TestData.SVNR_MARIA, AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL));
                eltern.add(new AngehoerigenBeziehung(TestData.SVNR_EBERHARD, TestData.SVNR_KURT, AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL));
                return eltern;
            }
            case TestData.SVNR_KURT -> {
                ArrayList<AngehoerigenBeziehung> beziehungen = new ArrayList<>();
                beziehungen.add(new AngehoerigenBeziehung(TestData.SVNR_KURT, TestData.SVNR_MARIA, AngehoerigenBeziehung.ANG_TYP_EHEPARTNER));
                beziehungen.add(new AngehoerigenBeziehung(TestData.SVNR_KURT, TestData.SVNR_ANGIE, AngehoerigenBeziehung.ANG_TYP_KIND));
                beziehungen.add(new AngehoerigenBeziehung(TestData.SVNR_KURT, TestData.SVNR_EBERHARD, AngehoerigenBeziehung.ANG_TYP_KIND));
                return beziehungen;
            }
            

        }
        return Collections.emptyList();
    }
}
