package at.gepardec.dojo.angehoerige;

public record AngehoerigenBeziehung(String personVsnr, String angehoerigerVsnr, Character angehoerigerTyp) {
    public static final char ANG_TYP_ELTERNTEIL = 'E';
    public static final Character ANG_TYP_EHEPARTNER = 'P';
    public static final Character ANG_TYP_KIND = 'K';
}
