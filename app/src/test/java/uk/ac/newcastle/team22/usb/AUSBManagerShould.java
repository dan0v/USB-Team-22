package uk.ac.newcastle.team22.usb;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.*;
import static org.junit.Assert.*;

/**
 * A test class for {@link USBManager}.
 *
 * @author Daniel Vincent
 * @version 1.0
 *
 * Separate test classes required to ensure instantiation checking in
 * {@link AUSBManagerShouldAlso#warnIfAlreadyInstantiated()}is not influenced by
 * {@link #warnIfNotInstantiated()}
 * (both tests change static variables which intentionally cannot be reset through code)
 *
 */
public class AUSBManagerShould {

    @Test(expected = IllegalArgumentException.class)
    public void warnIfNotInstantiated() {
        USBManager manager = USBManager.shared;
    }
}