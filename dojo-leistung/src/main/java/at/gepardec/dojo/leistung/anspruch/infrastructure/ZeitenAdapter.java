package at.gepardec.dojo.leistung.anspruch.infrastructure;

import at.gepardec.dojo.leistung.anspruch.domain.model.Versicherungszeit;
import at.gepardec.dojo.leistung.anspruch.domain.port.ZeitenPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;
import at.gepardec.dojo.zeiten.ZeitenService;

import java.util.List;
import java.util.Objects;

/**
 * Bindet das Versicherungswesen (Applikation MVB) an.
 * <p>
 * Der Umsystem-Typ {@code at.gepardec.dojo.zeiten.VersicherungsZeit} wird hier auf den
 * Domänentyp abgebildet und NICHT durchgereicht. Die Abbildung ist keine Doppelung, sondern die
 * Entkopplung: Ändert das Umsystem sein Modell, bleibt die Änderung in dieser Klasse.
 */
public class ZeitenAdapter implements ZeitenPort {

    private final ZeitenService zeitenService;

    public ZeitenAdapter(ZeitenService zeitenService) {
        this.zeitenService = Objects.requireNonNull(zeitenService, "zeitenService");
    }

    @Override
    public List<Versicherungszeit> versicherungszeiten(Svnr svnr) {
        Objects.requireNonNull(svnr, "svnr");

        return zeitenService.getVersicherungsZeiten(svnr.nummer()).stream()
                .map(zeit -> new Versicherungszeit(zeit.von(), zeit.bis()))
                .toList();
    }
}
