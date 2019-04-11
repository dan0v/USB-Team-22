package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

import static org.junit.Assert.*;

/**
 * A test class for {@link Resource}.
 *
 * @author Daniel Vincent
 * @author Dainel Walker
 * @version 1.0
 */
public class AResourceShould {

    @Test
    public void haveAType() {
        Resource computer = new Resource(ResourceType.COMPUTER, 5);
        assertEquals(ResourceType.COMPUTER, computer.getType());
    }

    @Test
    public void haveAStatus() {
        Resource computer = new Resource(ResourceType.COMPUTER, 5);
        assertEquals(5, computer.getAvailable());
    }

    @Test
    public void haveATotal() {
        Resource computer = new Resource(ResourceType.COMPUTER, 5, 5);
        assertEquals(5, computer.getTotal());
    }
}