package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import uk.ac.newcastle.team22.usb.coreUSB.ResourceType;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

import static org.junit.Assert.assertEquals;

/**
 * A test class for {@link uk.ac.newcastle.team22.usb.coreUSB.Resource}.
 *
 * @author Alexander MacLeod
 * @author Daniel Walker
 * @version 1.0
 */
public class AResourceTypeShould {

    /** Assert the correct {@link ResourceType} will be returned from an identifier. */
    @Test
    public void returnTypeFromIdentifier() throws FirestoreConstructable.InitialisationFailed {
        assertEquals(ResourceType.COMPUTER, ResourceType.valueFor("1"));
    }

    /** Assert the correct identifier will be returned from a given {@link ResourceType}. */
    @Test
    public void beAbleToReturnIdentifier () {
        assertEquals(4, ResourceType.PROJECTOR.getIdentifier());
    }
}
