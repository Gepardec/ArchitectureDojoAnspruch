package at.gepardec.dojo.anspruch.ports;

import at.gepardec.dojo.shared.domain.Svnr;
import at.gepardec.dojo.anspruch.domain.ZeitenStrecke;

import java.util.List;

public interface ZeitenService {
    List<ZeitenStrecke> getVersicherungsZeiten(Svnr svnr);
}
