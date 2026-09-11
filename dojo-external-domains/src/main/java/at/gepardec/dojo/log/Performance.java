package at.gepardec.dojo.log;

import at.gepardec.dojo.test.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Performance {
    private static final Logger log = LoggerFactory.getLogger(Performance.class);

    static public void logExternalCall( String service, String svnr){
        log.info("External Service call to {} for {}.", service, TestData.logInfo(svnr));
    }
}
