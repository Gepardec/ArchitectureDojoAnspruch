package at.gepardec.dojo.anspruch.adapters;

import at.gepardec.dojo.shared.domain.Svnr;
import at.gepardec.dojo.anspruch.domain.ZeitenStrecke;
import at.gepardec.dojo.anspruch.ports.ZeitenService;

import java.util.List;

public class ZeitenServiceImpl implements ZeitenService {
    private at.gepardec.dojo.zeiten.ZeitenService zsvc = new at.gepardec.dojo.zeiten.ZeitenService();

    @Override
    public List<ZeitenStrecke> getVersicherungsZeiten(Svnr svnr) {
        return zsvc.getVersicherungsZeiten(svnr.asString()).stream().map(
                        zeit -> new ZeitenStrecke(zeit.von(),zeit.bis()))
                .toList();
    }
}
