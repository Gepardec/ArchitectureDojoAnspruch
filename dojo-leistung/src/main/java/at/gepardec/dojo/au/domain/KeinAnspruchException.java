package at.gepardec.dojo.au.domain;

public class KeinAnspruchException extends RuntimeException {
    public KeinAnspruchException(String svnr) {
        super("Kein Anspruch für SVNR: " + svnr);
    }
}
