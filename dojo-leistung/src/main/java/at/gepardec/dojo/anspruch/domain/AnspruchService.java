package at.gepardec.dojo.anspruch.domain;


public class AnspruchService {
    private final EigenAnspruch eigenAnspruch = new EigenAnspruch();

    public boolean hasAnspruch(Svnr svnr) {
        if (eigenAnspruch.anspruch(svnr)){
            return true;
        }
        return false;
    }
}
