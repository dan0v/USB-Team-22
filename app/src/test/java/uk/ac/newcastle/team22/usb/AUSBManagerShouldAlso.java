package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A test class for {@link USBManager}.
 *
 * @author Daniel Vincent
 * @version 1.0
 *
 * Separate test classes required to ensure instantiation checking in
 * {@link #warnIfAlreadyInstantiated()}is not influenced by
 * {@link AUSBManagerShould#warnIfNotInstantiated()}
 * (both tests change static variables which intentionally cannot be reset through code)
 */
public class AUSBManagerShouldAlso {

    @Test(expected = IllegalArgumentException.class)
    public void warnIfAlreadyInstantiated() {
        List<Calendar> list1 = new ArrayList<Calendar>(); //Calendar lists must contain 7 elements to pass test
        List<Calendar> list2 = new ArrayList<Calendar>();
        List<Calendar> list3 = new ArrayList<Calendar>();
        List<Floor> list4 = new ArrayList<Floor>(); //can remain empty for test
        for(int i = 0; i < 7; i++) {
            Calendar test = Calendar.getInstance();
            test.setTimeInMillis(1000); //any value
            list1.add(test);
            list2.add(test);
            list3.add(test);
        }

        USBManager.prepareUSBManager(list1, list2, list3, list4); //first instance of USBManager

        USBManager.prepareUSBManager(list1, list2, list3, list4); //attempt second instance of USBManager
    }
}