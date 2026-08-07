package at.gepardec.dojo.leistung.shared.domain;

import at.gepardec.dojo.svnr.SvnrValidator;

public record Svnr(String nummer) {
    public Svnr(String nummer) {
        boolean valid = SvnrValidator.validate(nummer);
        if (!valid) throw new IllegalArgumentException("Ungültige Svnr: " + nummer);
        this.nummer = nummer;
    }
}
