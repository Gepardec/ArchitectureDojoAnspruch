package at.gepardec.dojo.personen;

import at.gepardec.dojo.test.TestData;

import java.time.LocalDate;

public class PersonenService {

    public Person getPerson(String svnr) {

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
                return new Person("Eduard", "Leonhardsberger",
                        LocalDate.of(2002, 4, 1));
            }
            case TestData.SVNR_ANGIE -> {
                return new Person("Angie", "Leonhardsberger",
                        LocalDate.of(2010, 7, 24));
            }

        }
        return null;
    }
}
