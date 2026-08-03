package at.gepardec.dojo.anspruch.domain;

import java.time.LocalDate;
import java.util.List;

public record ZeitenStrecke(LocalDate von, LocalDate bis) {

    public boolean isOpen() {
        return bis == null;
    }

    public static boolean hasOpen(List<ZeitenStrecke> zeiten) {
        for (ZeitenStrecke zeitenStrecke : zeiten) {
            if (zeitenStrecke.isOpen()) {
                return true;
            }
        }
        return false;
    }
}
