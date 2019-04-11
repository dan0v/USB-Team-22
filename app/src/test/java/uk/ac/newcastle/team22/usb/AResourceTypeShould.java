package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import uk.ac.newcastle.team22.usb.coreUSB.ResourceType;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;
import static org.junit.Assert.*;

public class AResourceTypeShould {

    /** Assert the correct ResourceType will be returned from it's identifier */
    @Test
    public void returnTypeFromIdentifier()throws FirestoreConstructable.InitialisationFailed {
            assertEquals(ResourceType.COMPUTER, ResourceType.valueFor("1"));
    }

    /** Assert the correct identifier will be returned from a given ResourceType */
    @Test
    public void beAbleToReturnIdentifier () {
        assertEquals(4, ResourceType.PROJECTOR.getIdentifier());
    }

}
