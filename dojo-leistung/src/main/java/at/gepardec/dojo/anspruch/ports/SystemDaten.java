package at.gepardec.dojo.anspruch.ports;

import java.time.LocalDate;

public interface SystemDaten {
    LocalDate today();

    void setToday(LocalDate today);

    void resetToday();
}
