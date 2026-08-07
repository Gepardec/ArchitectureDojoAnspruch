package at.gepardec.dojo.leistung.au.domain.port;

import at.gepardec.dojo.leistung.shared.domain.Svnr;

/**
 * Anti-Corruption Layer zum Anspruchskontext.
 * <p>
 * Der AU-Kontext formuliert hier, was <em>er</em> braucht. Ein Adapter im selben Kontext bildet
 * das auf den Input Port des Anspruchskontexts ab. Ändert sich dort die Signatur, ist genau eine
 * Klasse betroffen -- der Adapter.
 * <p>
 * Der Gegenentwurf steht in {@code feature/solution1}: dort ruft {@code AUService} direkt
 * {@code new AnspruchService()} eines fremden Kontexts auf.
 */
public interface AnspruchPruefungPort {

    boolean hatAnspruch(Svnr svnr);
}
