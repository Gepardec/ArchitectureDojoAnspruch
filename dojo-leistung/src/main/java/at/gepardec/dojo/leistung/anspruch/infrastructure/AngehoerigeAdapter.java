package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.angehoerige.AngehoerigeService;
import at.gepardec.dojo.angehoerige.AngehoerigenBeziehung;
import at.gepardec.dojo.leistung.anspruch.domain.port.AngehoerigePort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

import java.util.List;
import java.util.Objects;

/**
 * Bindet die Angehörigenbeziehungen an.
 * <p>
 * Die Kodierung der Beziehungsarten ({@code ANG_TYP_ELTERNTEIL}) bleibt hier. Die Domäne fragt
 * nach Elternteilen, nicht nach Beziehungssätzen mit einem Typkennzeichen.
 */
public class AngehoerigeAdapter implements AngehoerigePort {

    private final AngehoerigeService angehoerigeService;

    public AngehoerigeAdapter(AngehoerigeService angehoerigeService) {
        this.angehoerigeService = Objects.requireNonNull(angehoerigeService, "angehoerigeService");
    }

    @Override
    public List<Svnr> eltern(Svnr svnr) {
        Objects.requireNonNull(svnr, "svnr");

        return angehoerigeService.getAngehoerigenBeziehung(svnr.nummer()).stream()
                .filter(this::istElternteil)
                .map(beziehung -> new Svnr(beziehung.angehoerigerVsnr()))
                .toList();
    }

    private boolean istElternteil(AngehoerigenBeziehung beziehung) {
        // Character.equals statt ==, damit ein fehlender Typ nicht beim Entpacken knallt.
        return Character.valueOf(AngehoerigenBeziehung.ANG_TYP_ELTERNTEIL)
                .equals(beziehung.angehoerigerTyp());
    }
}
