package at.gepardec.dojo.anspruch.adapters;

import at.gepardec.dojo.anspruch.ports.AnspruchRegeln;

public class AnspruchRegelnImpl implements AnspruchRegeln {
    @Override
    public int getAltersgrenzeKind() {
        return 18;
    }
}
