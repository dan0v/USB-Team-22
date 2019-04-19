package uk.ac.newcastle.team22.usb;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import uk.ac.newcastle.team22.usb.coreUSB.Cafe;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;

/**
 * A test class for {@link uk.ac.newcastle.team22.usb.coreUSB.Cafe}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class ACafeShould {

    Cafe cafe;

    @Before
    public void setUp() {
        CafeMenuItem item = new CafeMenuItem(200, "sandwich");
        Cafe cafe = new Cafe(item);
    }

    @Test
    public void haveItems() {
        assertNotNull(cafe.getItems());
    }
}
