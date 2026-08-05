package at.gepardec.dojo.anspruch.ports;

import at.gepardec.dojo.anspruch.adapters.PersonenDatenServiceImpl;
import at.gepardec.dojo.anspruch.adapters.ZeitenServiceImpl;

/**
 * For Simplicity in this demo use this factory instead of real dependency injcection.
 */
public class DomainPortFactory {

    public static ZeitenService getZeitenService() {
        return new ZeitenServiceImpl();
    }

    public static PersonenDatenService getPersonenDatenService() {
        return new PersonenDatenServiceImpl();
    }
}