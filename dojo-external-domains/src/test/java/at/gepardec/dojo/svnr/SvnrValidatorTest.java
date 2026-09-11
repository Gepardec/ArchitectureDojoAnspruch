package at.gepardec.dojo.svnr;

import at.gepardec.dojo.test.TestData;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class SvnrValidatorTest {
    private static final Logger log = LoggerFactory.getLogger(SvnrValidatorTest.class);

    @Test
    public void testValidateSvnr() throws Exception{
        assertTrue(SvnrValidator.validate(TestData.SVNR_KURT));
        assertTrue(SvnrValidator.validate(TestData.SVNR_MARIA));
        assertTrue(SvnrValidator.validate(TestData.SVNR_EBERHARD));
        assertTrue(SvnrValidator.validate(TestData.SVNR_ANGIE));
    }

    @Test
    public void testGenerateSvnr() throws Exception{
        log.info( "Generated SVNR: {}", SvnrValidator.generateSvnr("250383"));
        log.info( "Generated SVNR: {}", SvnrValidator.generateSvnr("130985"));
        log.info( "Generated SVNR: {}", SvnrValidator.generateSvnr("240710"));
        log.info( "Generated SVNR: {}", SvnrValidator.generateSvnr("010402"));
        log.info( "Generated SVNR: {}", SvnrValidator.generateSvnr("310761"));
    }
}