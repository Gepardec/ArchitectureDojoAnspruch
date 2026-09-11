package at.gepardec.dojo.personen;

import at.gepardec.dojo.log.Performance;
import at.gepardec.dojo.test.TestData;

import java.time.LocalDate;

public class PersonenService {

    public Person getPerson(String svnr) {
        Performance.logExternalCall("getPerson", svnr);

        switch (svnr){
            case TestData.SVNR_KURT -> {
                return new Person("Kurt", "Leonhardsberger",
                        LocalDate.of(1983, 3, 25));
            }
            case TestData.SVNR_MARIA -> {
                return new Person("Maria", "Leonhardsberger",
                        LocalDate.of(1985, 9, 13));
            }
            case TestData.SVNR_EBERHARD -> {
                return new Person("Eberhard", "Leonhardsberger",
                        LocalDate.of(2002, 4, 1));
            }
            case TestData.SVNR_ANGIE -> {
                return new Person("Angie", "Leonhardsberger",
                        LocalDate.of(2010, 7, 24));
            }
            case TestData.SVNR_OMAMA -> {
                return new Person("Omama", "Leonhardsberger",
                        LocalDate.of(1961, 7, 31));
            }

        }
        return null;
    }
}
