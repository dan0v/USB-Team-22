package uk.ac.newcastle.team22.usb.coreUSB;

import uk.ac.newcastle.team22.usb.firebase.FirestoreCompletionHandler;

/**
 * A class which manages the current version, caching and updates for the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBManager {

    /** The shared instance of the Urban Sciences Building manager. */
    public static USBManager shared = new USBManager();

    /** The update manager for the Urban Sciences Building. */
    private USBUpdateManager updateManager;

    /** The Urban Sciences Building. */
    private USB building;

    /**
     * Prepares the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been retrieved.
     */
    public void prepareBuilding(final USBUpdateManager.UpdateCompletionHandler handler) {
        updateManager.requestCached(new FirestoreCompletionHandler<USBUpdateManager.USBUpdate>() {
            @Override
            public void completed(USBUpdateManager.USBUpdate cached) {
                building = new USB(cached);
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
        updateManager.update(new FirestoreCompletionHandler<USBUpdateManager.USBUpdate>() {
            @Override
            public void completed(USBUpdateManager.USBUpdate update) {
                building = new USB(update);
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
