package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.ResourceType;

import static org.junit.Assert.assertEquals;

/**
 * A test class for {@link Resource}.
 *
 * @author Daniel Vincent
 * @author Dainel Walker
 * @version 1.0
 */
public class AResourceShould {

    /** Assert a resource is of one of the types defined in the enum 'ResourceType' */
    @Test
    public void haveAType() {
        Resource computer = new Resource(ResourceType.COMPUTER, 5);
        assertEquals(ResourceType.COMPUTER, computer.getType());
    }

    /** Assert a resource can display the total number available */
    @Test
    public void haveAStatus() {
        Resource computer = new Resource(ResourceType.COMPUTER, 5);
        assertEquals(5, computer.getAvailable());
    }

    /** Assert a resource can display the total number present */
    @Test
    public void haveATotal() {
        Resource computer = new Resource(ResourceType.COMPUTER, 5, 5);
        assertEquals(5, computer.getTotal());
    }
}