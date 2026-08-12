package at.gepardec.dojo.application;

import at.gepardec.dojo.anspruch.ports.*;

public class Application {

    public static <T> T getInstance(String className, Class<T> expectedType)  {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (!expectedType.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    className + " is not a " + expectedType.getName());
        }

        try {
            return expectedType.cast(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        DomainPortFactory.setAnspruchRegeln(Application.getInstance(
                "at.gepardec.dojo.anspruch.adapters.AnspruchRegelnImpl",
                AnspruchRegeln.class));
        DomainPortFactory.setPersonenDatenService(Application.getInstance(
                "at.gepardec.dojo.anspruch.adapters.PersonenDatenServiceImpl",
                PersonenDatenService.class));
        DomainPortFactory.setZeitenService(Application.getInstance(
                "at.gepardec.dojo.anspruch.adapters.ZeitenServiceImpl",
                ZeitenService.class));
        DomainPortFactory.setSystemDaten(Application.getInstance(
                "at.gepardec.dojo.anspruch.adapters.SystemDatenImpl",
                SystemDaten.class));
    }
}
