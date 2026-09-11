package at.gepardec.dojo.test;

public class TestData {
    public static final String SVNR_KURT = "1454250383";
    public static final String SVNR_MARIA = "1174130985";
    public static final String SVNR_EBERHARD = "1582240710";
    public static final String SVNR_ANGIE = "1914010402";
    public static final String SVNR_OMAMA = "1411310761";

    public static String logInfo(String svnr){
        return String.format("Svnr: %s, Name: %s", svnr, name(svnr));
    }

    private static String name(String svnr) {
        return switch (svnr){
            case SVNR_KURT -> "Kurt";
            case SVNR_MARIA -> "Maria";
            case SVNR_EBERHARD -> "Eberhard";
            case SVNR_ANGIE -> "Angie";
            default -> "Unbekannte Svnr";
        };
    }
}
