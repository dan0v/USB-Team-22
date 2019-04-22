package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import uk.ac.newcastle.team22.usb.coreUSB.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test class for {@link uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItemCategory}.
 *
 * @author Moeez Shahid
 * @version 1.0
 */

public class ACafeMenuItemCategoryShould {

    /** Assert that a category has a name. */
    @Test
    public void haveAName() {
        CafeMenuItemCategory testCategory = new CafeMenuItemCategory("Test Category", 5);
        assertEquals("Test Category", testCategory.getName());
    }

    /** Assert that a category must have an icon. */
    @Test
    public void haveAnIconIdentifier() {
        CafeMenuItemCategory testCategory = new CafeMenuItemCategory("Test Category", 5);
        assertNotNull(testCategory.getIcon());
    }

}
