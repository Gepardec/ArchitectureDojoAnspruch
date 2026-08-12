package at.gepardec.dojo.application;

import at.gepardec.dojo.anspruch.ports.AnspruchRegeln;
import at.gepardec.dojo.anspruch.ports.DomainPortFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationTest {
    @BeforeEach
    void setUp() {
        Application.init();
    }

    @Test
    void testPortInit() {
        AnspruchRegeln anspruchRegeln = Application.getInstance(
                "at.gepardec.dojo.anspruch.adapters.AnspruchRegelnImpl",
                AnspruchRegeln.class);
        assertNotNull(anspruchRegeln);
    }

    @Test
    void testInitializePortFactory() {
        Application.init();
        assertNotNull(DomainPortFactory.getAnspruchRegeln());
    }

}
