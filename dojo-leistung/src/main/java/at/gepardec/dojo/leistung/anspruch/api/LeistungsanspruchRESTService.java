package at.gepardec.dojo.leistung.anspruch.api;

import at.gepardec.dojo.leistung.anspruch.application.port.PruefeLeistungsanspruchUseCase;

public class LeistungsanspruchRESTService {
    private PruefeLeistungsanspruchUseCase useCase;

    public Response<String> pruefeAnspruch(String vsnr) {
        // if(useCase.pruefe(...)) return "hat Anspruch"
        // return "hat keinen Anspruch"
        return null;
    }
}
