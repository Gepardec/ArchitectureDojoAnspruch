package at.gepardec.dojo.leistung.au.infrastructure;

import at.gepardec.dojo.leistung.anspruch.application.port.PruefeLeistungsanspruchUseCase;
import at.gepardec.dojo.leistung.au.application.port.LeistungsanspruchPruefungPort;
import at.gepardec.dojo.leistung.shared.domain.Svnr;

public class LeistungsanspruchPruefungAdapter implements LeistungsanspruchPruefungPort {
    private final PruefeLeistungsanspruchUseCase useCase;

    public LeistungsanspruchPruefungAdapter(PruefeLeistungsanspruchUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public boolean pruefeAnspruch(Svnr svnr) {
        return useCase.hatLeistungsanspruch(svnr);
    }
}
