package at.gepardec.dojo.svnr;


import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SvnrValidator {

    private static final Logger log = LoggerFactory.getLogger(SvnrValidator.class);

    private static final int[] WEIGHTS = {3, 7, 9, 0, 5, 8, 4, 2, 1, 6};
    private static final Pattern REGEX = Pattern.compile("^[0-9]+$");

    private SvnrValidator() {
        // Verhindert Instanziierung
    }

    public static boolean validate(String svnr) {
        if (svnr == null
                || svnr.length() != 10
                || !REGEX.matcher(svnr).matches()
                || svnr.charAt(0) == '0') {
            log.error("Svnr Eingabe syntaktisch falsch (sollen 10 Zeichen sein):" + svnr);
            return false;
        }

        int soll = calculateChecksum(svnr);
        int ist = Character.getNumericValue(svnr.charAt(3));
        if ( soll != ist){
            log.error("Prüfsumme von {} ist {} und nicht {}!", svnr, ist, soll);
        }
        return soll == ist;
    }

    private static int calculateChecksum(String svnr) {
        int sum = 0;

        for (int i = 0; i < svnr.length(); i++) {
            int digit = Character.getNumericValue(svnr.charAt(i));
            sum += WEIGHTS[i] * digit;
        }

        return sum % 11;
    }

    public static String generateSvnr(String gebDat){
        if (gebDat == null
                || gebDat.length() != 6
                || !REGEX.matcher(gebDat).matches()) {
            throw new RuntimeException("Geburtsdatum nicht korrkt (6 stellig numerisch): " + gebDat);
        }

        String laufNr = String.valueOf(Math.random() * 10000 + 9999).substring(0,4);
        String candidate = laufNr + gebDat;

        String check = String.valueOf(calculateChecksum(candidate)).substring(0,1);
        candidate = candidate.substring(0,3) + check + candidate.substring(4,10);
        if (!validate(candidate)){
            return generateSvnr(gebDat);
        }
        return candidate;
    }
}