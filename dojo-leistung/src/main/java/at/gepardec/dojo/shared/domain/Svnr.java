package at.gepardec.dojo.shared.domain;

public class Svnr {
    private final String svnr;

    public Svnr(String svnr) {
        this.svnr = svnr;
    }

    public String asString() {
        return svnr;
    }
}
