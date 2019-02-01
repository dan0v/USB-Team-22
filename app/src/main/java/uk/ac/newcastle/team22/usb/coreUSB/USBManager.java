package uk.ac.newcastle.team22.usb.coreUSB;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.ac.newcastle.team22.usb.firebase.FirestoreCompletionHandler;

/**
 * A class which represents the Urban Sciences Building, stores a list of floors,
 * and deals with building state changes based on current device time.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBManager {

    /** The shared instance of the Urban Sciences Building. */
    public static USBManager shared = new USBManager();

    /** The update manager for USB */
    private USBUpdateManager updateManager;

    /** The Urban Sciences Building */
    private USB building;

    /**
     * Prepares the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been retrieved.
     */
    public void prepareBuilding(final USBUpdateManager.UpdateCompletionHandler handler) {
        updateManager.requestCached(new FirestoreCompletionHandler<USB>() {
            @Override
            public void completed(USB updatedBuilding) {
                building = updatedBuilding;
                handler.loadedFromCache();
            }
            @Override
            public void failed(Exception exception) {
                boolean forceUpdate = (exception instanceof USBUpdateManager.USBNoCachedVersionAvailable);
                handler.requiresUpdate(forceUpdate);
            }
        });
    }

    /**
     * Updates the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been updated.
     */
    public void updateBuilding(final USBUpdateManager.UpdateCompletionHandler handler) {
        updateManager.update(new FirestoreCompletionHandler<USB>() {
            @Override
            public void completed(USB updatedBuilding) {
                building = updatedBuilding;
                handler.loadedFromCache();
            }
            @Override
            public void failed(Exception exception) {
                handler.requiresUpdate(true);
            }
        });
    }

    /**
     * @return The Urban Sciences Building.
     */
    public USB getBuilding() {
        return building;
    }

    /** Constructor. */
    private USBManager() {
        updateManager = new USBUpdateManager();
    }
}
