package at.gepardec.dojo.anspruch.ports;

import at.gepardec.dojo.anspruch.adapters.AnspruchRegelnImpl;
import at.gepardec.dojo.anspruch.adapters.PersonenDatenServiceImpl;
import at.gepardec.dojo.anspruch.adapters.SystemDatenImpl;
import at.gepardec.dojo.anspruch.adapters.ZeitenServiceImpl;

/**
 * For Simplicity in this demo use this factory instead of real dependency injcection.
 */
public class DomainPortFactory {
    private static SystemDaten systemDaten = new SystemDatenImpl();

    public static ZeitenService getZeitenService() {
        return new ZeitenServiceImpl();
    }

    public static PersonenDatenService getPersonenDatenService() {
        return new PersonenDatenServiceImpl();
    }

    public static AnspruchRegeln getAnspruchRegeln() {
        return new AnspruchRegelnImpl();
    }

    public static SystemDaten getSystemDaten() {
        return systemDaten;
    }
}