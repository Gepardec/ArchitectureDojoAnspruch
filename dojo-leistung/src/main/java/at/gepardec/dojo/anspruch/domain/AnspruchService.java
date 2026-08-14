package at.gepardec.dojo.anspruch.domain;


import at.gepardec.dojo.shared.domain.Svnr;

public class AnspruchService {
    private final Anspruch eigenAnspruch = new EigenAnspruch();
    private final Anspruch kindAnspruch = new KindAnspruch();

    public boolean hasAnspruch(Svnr svnr) {

        if (eigenAnspruch.anspruch(svnr)){
            return true;
        }
        if (kindAnspruch.anspruch(svnr)){
            return true;
        }
        return false;
    }
}
