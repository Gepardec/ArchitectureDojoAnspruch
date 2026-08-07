package at.gepardec.dojo.leistung.shared.domain;

import at.gepardec.dojo.svnr.SvnrValidator;

/**
 * Versicherungsnummer als Wertobjekt.
 * <p>
 * Bewusst im Shared Kernel: Beide Kontexte (Anspruch und AU) brauchen den Typ. Läge er in
 * {@code anspruch.domain}, müsste der AU-Kontext auf die Domäne eines fremden Kontexts zugreifen.
 * <p>
 * {@link SvnrValidator} stammt aus dem Fremdmodul {@code dojo-external-domains} und ist die
 * einzige zugelassene Ausnahme im Domänenring: eine zustandslose Utility, kein Umsystem.
 * Die Architekturregel führt sie namentlich.
 */
public record Svnr(String nummer) {

    public Svnr {
        if (!SvnrValidator.validate(nummer)) {
            throw new IllegalArgumentException("Ungültige Versicherungsnummer: " + nummer);
        }
    }

    @Override
    public String toString() {
        return nummer;
    }
}
