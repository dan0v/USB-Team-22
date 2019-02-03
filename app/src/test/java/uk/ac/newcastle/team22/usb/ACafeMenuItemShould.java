package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import static org.junit.Assert.*;

/**
 * A test class for {@link CafeMenuItem}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class ACafeMenuItemShould {

    @Test
    public void haveAnItemName() {
        CafeMenuItem item = new CafeMenuItem(200, "sandwich");
        assertEquals("sandwich", item.getItemName());
    }

    @Test
    public void haveAnItemPrice() {
        CafeMenuItem item = new CafeMenuItem(200, "sandwich");
        assertEquals(200, item.getPrice());
    }
}
