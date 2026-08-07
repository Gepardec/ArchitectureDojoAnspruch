package at.gepardec.dojo.leistung;

import at.gepardec.dojo.leistung.anspruch.infrastructure.SystemZeitAdapter;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Fitness Function für die Zielvariante <b>Onion Architecture (Palermo)</b>.
 * <p>
 * Diese Klasse ist die ausführbare Fassung der Architekturentscheidung. Sie entsteht bewusst
 * früh: Am Ende geschrieben wäre sie ein Stempel, hier schlägt sie beim ersten falschen Import zu.
 */
class ArchitectureRulesTest {

    private static final String BASIS = "at.gepardec.dojo.leistung";

    private final JavaClasses klassen = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(BASIS);

    /**
     * Das Definitionsmerkmal von Onion: Der Domänenring besitzt seine Ports und kennt nichts
     * außerhalb seiner selbst.
     * <p>
     * Zugelassen sind neben dem eigenen Paket nur der Shared Kernel und die JDK-Basis.
     * {@code at.gepardec.dojo.svnr.SvnrValidator} ist die einzige benannte Ausnahme aus dem
     * Fremdmodul -- eine zustandslose Utility, kein Umsystem. Sie wird ausschließlich vom Shared
     * Kernel genutzt und ist deshalb hier nicht zugelassen.
     */
    @Test
    void domaenenringHaengtAnNichtsAusserhalb() {
        ArchRule regel = classes()
                .that().resideInAPackage(BASIS + ".anspruch.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        BASIS + ".anspruch.domain..",
                        BASIS + ".shared.domain..",
                        "java..")
                .because("Onion: Abhängigkeiten zeigen nach innen, der Domänenring kennt weder "
                        + "Application noch Infrastructure noch Umsysteme");

        regel.check(klassen);
    }

    @Test
    void auDomaenenringHaengtAnNichtsAusserhalb() {
        ArchRule regel = classes()
                .that().resideInAPackage(BASIS + ".au.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        BASIS + ".au.domain..",
                        BASIS + ".shared.domain..",
                        "java..")
                .because("Onion: gilt für jeden Kontext gleichermaßen");

        regel.check(klassen);
    }

    /**
     * Der Shared Kernel darf zusätzlich den {@code SvnrValidator} nutzen. Diese Regel hält die
     * Ausnahme fest, statt sie stillschweigend zuzulassen.
     */
    @Test
    void sharedKernelNutztNurDieBenannteAusnahme() {
        ArchRule regel = classes()
                .that().resideInAPackage(BASIS + ".shared.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        BASIS + ".shared.domain..",
                        "at.gepardec.dojo.svnr..",
                        "java..")
                .because("SvnrValidator ist eine zustandslose Utility und die einzige "
                        + "zugelassene Ausnahme aus dojo-external-domains");

        regel.check(klassen);
    }

    /**
     * Kontextgrenze: Der AU-Kontext darf den Anspruchskontext ausschließlich über seinen eigenen
     * Adapter erreichen. Domäne und Application des AU-Kontexts kennen ihn nicht.
     * <p>
     * In {@code feature/solution1} ruft {@code au.domain.AUService} direkt
     * {@code new AnspruchService()} -- Domäne zu Domäne über die Kontextgrenze hinweg.
     */
    @Test
    void auKenntAnspruchNurAusDerInfrastruktur() {
        ArchRule regel = noClasses()
                .that().resideInAnyPackage(BASIS + ".au.domain..", BASIS + ".au.application..")
                .should().dependOnClassesThat().resideInAPackage(BASIS + ".anspruch..")
                .because("die Kontextgrenze läuft über au.domain.port.AnspruchPruefungPort und "
                        + "den zugehörigen Adapter, nicht über direkte Zugriffe");

        regel.check(klassen);
    }

    /**
     * Die Systemuhr ist eine Infrastrukturressource. Holt sich die Domäne das Tagesdatum selbst,
     * ist ihre Fachlogik nicht mehr zu einem beliebigen Stichtag prüfbar -- der Fehler, an dem
     * {@code feature/dojo1} in {@code Versicherter.isUnter18()} scheitert.
     */
    @Test
    void keineSystemuhrImDomaenenring() {
        ArchRule regel = noClasses()
                .that().resideInAPackage(BASIS + "..domain..")
                .should().callMethod(LocalDate.class, "now")
                .because("die Domäne bekommt den Stichtag gereicht, sie besorgt ihn sich nicht");

        regel.check(klassen);
    }

    /**
     * Seit {@code add-stichtag-port} (Iteration 7) verschärft: Die Systemuhr darf nirgends mehr
     * stehen außer im dafür vorgesehenen Adapter.
     * <p>
     * Die vorherige Fassung prüfte nur den Domänenring und hätte den Aufruf im Application
     * Service durchgelassen -- genau den, den Iteration 7 entfernt.
     */
    @Test
    void systemuhrNurImZeitAdapter() {
        ArchRule regel = noClasses()
                .that().doNotBelongToAnyOf(SystemZeitAdapter.class)
                .should().callMethod(LocalDate.class, "now")
                .because("das Tagesdatum kommt über den ZeitPort, damit Testfälle zeitstabil sind");

        regel.check(klassen);
    }
}
