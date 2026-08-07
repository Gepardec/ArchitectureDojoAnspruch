package at.gepardec.dojo.leistung.anspruch.application;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

/**
 * Einstiegspunkt in den Anspruchskontext (Input Port).
 * <p>
 * Onion verlangt keinen Input Port. Er wird trotzdem eingeführt, weil zwei Treiber auf diesen
 * Kontext zugreifen -- der Web-Einstieg und, über einen Adapter, der AU-Kontext. Ohne Interface
 * müsste der AU-Kontext eine konkrete Klasse eines fremden Kontexts kennen.
 */
public interface PruefeAnspruchUseCase {

    boolean hatAnspruch(Svnr svnr);
}
