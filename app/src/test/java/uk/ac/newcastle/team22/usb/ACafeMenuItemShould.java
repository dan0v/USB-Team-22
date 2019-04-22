package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.*;
import static org.junit.Assert.*;

/**
 * A test class for {@link CafeMenuItem}.
 *
 * @author Patrick Lindley
 * @author Moeez Shahid
 * @version 1.1
 */
public class ACafeMenuItemShould {

    /** Assert that an item has a name. */
    @Test
    public void haveAName() {
        CafeMenuItemCategory testCategory = new CafeMenuItemCategory("Test Category", 5);
        CafeMenuItem item = new CafeMenuItem("sandwich", 200, testCategory, false );
        assertEquals("sandwich", item.getName());
    }

    /** Assert that an item has a price. */
    @Test
    public void haveAPrice() {
        CafeMenuItemCategory testCategory = new CafeMenuItemCategory("Test Category", 5);
        CafeMenuItem item = new CafeMenuItem("sandwich", 200, testCategory, false );
        assertEquals(200, item.getPrice());
    }

    /** Assert that an item has a category. */
    @Test
    public void haveACategory() {
        CafeMenuItemCategory testCategory = new CafeMenuItemCategory("Test Category", 5);
        CafeMenuItem item = new CafeMenuItem("sandwich", 200, testCategory, false );
        assertEquals(testCategory, item.getCategory());
    }

    /** Assert that an item can be part of a meal deal. */
    @Test
    public void haveAMealDeal() {
        CafeMenuItemCategory testCategory = new CafeMenuItemCategory("Test Category", 5);
        CafeMenuItem item = new CafeMenuItem("sandwich", 200, testCategory, true);
        assertTrue(item.isMealDeal());
    }

}
