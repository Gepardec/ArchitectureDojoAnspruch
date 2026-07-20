package at.gepardec.dojo.personen;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.angehoerige.AngehoerigenBeziehung;
import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class PersonenTest {

    private PersonenService persSvc = new PersonenService();
    private AngehoerigeService angSvc = new AngehoerigeService();

    @Test
    void testAngieIstMinderjährig() {
        Person angie = persSvc.getPerson(TestData.SVNR_ANGIE);
        assertNotNull(angie, "Angie hat Personendaten");
        assertEquals( 15, age(angie), "Angie ist 15 Jahre alt");
    }

    @Test
    void testAllPersonenExist() {
        assertNotNull(persSvc.getPerson(TestData.SVNR_KURT), "Kurt hat Personendaten");
        assertNotNull(persSvc.getPerson(TestData.SVNR_MARIA), "Maria hat Personendaten");
        assertNotNull(persSvc.getPerson(TestData.SVNR_EBERHARD), "Eberhard hat Personendaten");
        assertNotNull(persSvc.getPerson(TestData.SVNR_ANGIE), "Angie hat Personendaten");
    }

    @Test
    void testAngieHasParents() {
        assertTrue(hasParent(TestData.SVNR_ANGIE), "Angie hat Eltern");
    }

    private boolean hasParent(String svnr) {
        for (AngehoerigenBeziehung angBez: angSvc.getAngehoerigenBeziehung(svnr)){
            if ( angBez.angehoerigerTyp().equals(AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL)){
                return true;
            }
        }
        return false;
    }

    private int age(Person person) {
        return Period.between(person.geburtsDatum(),
                LocalDate.of(2026, 7, 18)).getYears();
    }
}