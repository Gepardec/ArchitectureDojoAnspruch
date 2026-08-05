package at.gepardec.dojo.anspruch.adapters;

import at.gepardec.dojo.anspruch.ports.SystemDaten;

import java.time.LocalDate;

public class SystemDatenImpl implements SystemDaten {
    private LocalDate today;

    @Override
    public LocalDate today() {
        return today != null ? today : LocalDate.now();
    }

    @Override
    public void setToday(LocalDate today) {
        this.today = today;
    }

    @Override
    public void resetToday() {
        today = null;
    }
}
