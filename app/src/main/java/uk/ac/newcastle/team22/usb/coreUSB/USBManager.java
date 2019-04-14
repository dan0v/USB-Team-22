package uk.ac.newcastle.team22.usb.coreUSB;

import uk.ac.newcastle.team22.usb.coreApp.JSONDataFetcher;
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

    /** The data fetcher for NUIT JSON data. */
    private JSONDataFetcher dataFetcher = new JSONDataFetcher();

    /**
     * Prepares the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been retrieved.
     */
    public void prepareBuilding(final USBUpdateManager.UpdateCompletionHandler handler) {
        updateManager.requestCached(new FirestoreCompletionHandler<USBUpdate>() {
            @Override
            public void completed(USBUpdate cached) {
                building = new USB(cached);

                // Check for updates to the Urban Sciences Building.
                updateManager.checkForUpdate(building.getConfiguration().getVersion(),new FirestoreCompletionHandler<Boolean>() {
                    @Override
                    public void completed(Boolean updateRequired) {
                        super.completed(updateRequired);

                        // Determine whether an update is required.
                        if (updateRequired) {
                            updateManager.update(new FirestoreCompletionHandler<USBUpdate>() {
                                @Override
                                public void completed(USBUpdate usbUpdate) {
                                    super.completed(usbUpdate);
                                    building = new USB(usbUpdate);
                                    handler.loadedFromCache();
                                }
                                @Override
                                public void failed(Exception exception) {
                                    super.failed(exception);
                                    handler.loadedFromCache();
                                }
                            });
                        } else {
                            handler.loadedFromCache();
                        }
                    }
                    @Override
                    public void failed(Exception exception) {
                        super.failed(exception);
                        handler.loadedFromCache();
                    }
                });
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
        updateManager.update(new FirestoreCompletionHandler<USBUpdate>() {
            @Override
            public void completed(USBUpdate update) {
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

    /**
     * Helper method for setting a custom Urban Sciences building for testing.
     *
     * @param building The Urban Sciences Building.
     */
    public void setBuilding(USB building) {
        this.building = building;
    }

    /**
     * Download and update local data using the Urban Sciences Building computer availability JSON provided by NUIT.
     */
    public void updateComputerAvailability() {
        dataFetcher.execute();
    }

    /** Constructor. */
    private USBManager() {
        updateManager = new USBUpdateManager();
    }
}
