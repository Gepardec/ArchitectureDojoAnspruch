package at.gepardec.dojo.anspruch.uc;

import at.gepardec.dojo.anspruch.domain.AnspruchService;
import at.gepardec.dojo.shared.domain.Svnr;

public class AnspruchWebCheck {
    private AnspruchService anspruchService = new AnspruchService();

    public String check(String svnr) {

        if (anspruchService.hasAnspruch(new Svnr(svnr))){
            return "Anspruch vorhanden";
        }
        return "Kein Anspruch";
    }
}
