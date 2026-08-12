package at.gepardec.dojo.anspruch.ports;

import at.gepardec.dojo.anspruch.adapters.AnspruchRegelnImpl;
import at.gepardec.dojo.anspruch.adapters.PersonenDatenServiceImpl;
import at.gepardec.dojo.anspruch.adapters.SystemDatenImpl;
import at.gepardec.dojo.anspruch.adapters.ZeitenServiceImpl;

/**
 * For Simplicity in this demo use this factory instead of real dependency injcection.
 */
public class DomainPortFactory {
    private static SystemDaten systemDaten;
    private static AnspruchRegeln anspruchRegeln;
    private static PersonenDatenService personenDatenService;
    private static ZeitenService zeitenService;


    public static PersonenDatenService getPersonenDatenService() {
        return personenDatenService;
    }

    public static ZeitenService getZeitenService() {
        return zeitenService;
    }

    public static AnspruchRegeln getAnspruchRegeln() {
        return anspruchRegeln;
    }

    public static SystemDaten getSystemDaten() {
        return systemDaten;
    }


    public static void setPersonenDatenService(PersonenDatenService instance) {
        personenDatenService = instance;
    }
    public static void setZeitenService(ZeitenService instance) {
        zeitenService = instance;
    }

    public static void setSystemDaten(SystemDaten instance) {
        systemDaten = instance;
    }
    public static void setAnspruchRegeln(AnspruchRegeln instance) {
        anspruchRegeln = instance;
    }
}