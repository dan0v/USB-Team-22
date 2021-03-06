package uk.ac.newcastle.team22.usb.coreUSB;

import android.util.Log;

import java.util.List;

import uk.ac.newcastle.team22.usb.firebase.FirebaseManager;
import uk.ac.newcastle.team22.usb.firebase.FirestoreCompletionHandler;
import uk.ac.newcastle.team22.usb.firebase.FirestoreDatabaseCollection;
import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A class which manages and maintains new and cached versions of the Urban Sciences Building.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBUpdateManager {

    /** Boolean value whether the cache is enabled. Used for debugging purposes. */
    private static final boolean CACHED_ENABLED = true;

    /** The exception to throw when a cached version of the Urban Sciences Building is not available. */
    public class USBNoCachedVersionAvailable extends Exception {}

    /** The exception to throw when the cache of the Urban Sciences Building is disabled. */
    public class USBCachedDisabled extends Exception {}

    /**
     * Requests a cached version of the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been retrieved.
     */
    public void requestCached(final FirestoreCompletionHandler<USBUpdate> handler) {
        // Check if the cache is enabled.
        if (!CACHED_ENABLED) {
            handler.failed(new USBCachedDisabled());
            return;
        }

        // Disable network access to force application to load cached data, if available.
        FirebaseManager.shared.enableCache(new FirestoreCompletionHandler<Void>() {
            @Override
            public void completed(Void aVoid) {
                super.completed(aVoid);

                // Request the cached version of the Urban Sciences Building.
                update(new FirestoreCompletionHandler<USBUpdate>() {

                    // A cached version of the Urban Sciences Building was available.
                    @Override
                    public void completed(final USBUpdate usbUpdate) {
                        super.completed(usbUpdate);
                        didFinishLoadingFromCache(new FirestoreCompletionHandler<Void>() {
                            @Override
                            public void completed(Void aVoid) {
                                handler.completed(usbUpdate);
                            }
                        });
                    }

                    // Unable to retrieve cached version of the Urban Sciences Building.
                    @Override
                    public void failed(Exception exception) {
                        super.failed(exception);
                        didFinishLoadingFromCache(new FirestoreCompletionHandler<Void>() {
                            @Override
                            public void completed(Void aVoid) {
                                super.completed(aVoid);
                                handler.failed(new USBNoCachedVersionAvailable());
                            }
                        });
                    }

                    /** Called once the Urban Sciences Building has loaded from cache. */
                    private void didFinishLoadingFromCache(final FirestoreCompletionHandler<Void> handler) {
                        // Re-enable network access for future requests.
                        FirebaseManager.shared.disableCache(new FirestoreCompletionHandler<Void>() {
                            @Override
                            public void completed(Void aVoid) {
                                super.completed(aVoid);
                                handler.completed(null);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Checks whether an update is available for the Urban Sciences Building.
     *
     * @param currentVersion The current version of the Urban Sciences Building.
     * @param handler The completion handler called once the Urban Sciences Building has been
     *                checked for an update.
     */
    public void checkForUpdate(final int currentVersion, final FirestoreCompletionHandler<Boolean> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.CONFIGURATION, null, new FirestoreCompletionHandler<List<USBConfiguration>>() {
            @Override
            public void completed(List<USBConfiguration> usbConfigurations) {
                super.completed(usbConfigurations);
                if (usbConfigurations.size() == 1) {
                    USBConfiguration config = usbConfigurations.get(0);
                    handler.completed(currentVersion < config.getVersion());
                    return;
                }
                handler.failed(new Exception("A single configuration file was not found"));
            }

            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB Configuration", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Requests an update to the Urban Sciences Building.
     * Ensures that the user is authenticated to receive an update.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been updated.
     */
    public void update(final FirestoreCompletionHandler<USBUpdate> handler) {
        // Authenticate user before attempting to download update.
        FirebaseManager.shared.authenticate(new FirestoreCompletionHandler<Void>() {
            @Override
            public void completed(Void aVoid) {
                super.completed(aVoid);

                // Start Urban Sciences Building update.
                loadBuilding(new FirestoreCompletionHandler<USBUpdate>() {
                    @Override
                    public void completed(USBUpdate usbUpdate) {
                        super.completed(usbUpdate);
                        handler.completed(usbUpdate);
                    }
                    @Override
                    public void failed(Exception exception) {
                        super.failed(exception);
                        handler.failed(exception);
                    }
                });
            }
            @Override
            public void failed(Exception exception) {
                super.failed(exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Updates the floors, rooms, staff members, café menu, opening hours and navigation nodes
     * in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been updated.
     */
    private void loadBuilding(final FirestoreCompletionHandler<USBUpdate> handler) {
        final USBUpdate update = new USBUpdate();

        // Request updated floors and rooms.
        loadFloors(new FirestoreCompletionHandler<List<Floor>>() {
            @Override
            public void completed(List<Floor> floors) {
                update.setFloors(floors);

                // Request updated staff members.
                loadStaffMembers(new FirestoreCompletionHandler<List<StaffMember>>() {
                    @Override
                    public void completed(List<StaffMember> staffMembers) {
                        update.setStaffMembers(staffMembers);

                        // Request café menu items.
                        loadCafeMenuItems(new FirestoreCompletionHandler<List<CafeMenuItem>>() {
                            @Override
                            public void completed(List<CafeMenuItem> menuItems) {
                                update.setCafeMenuItems(menuItems);

                                // Request navigation nodes.
                                loadNavigationNodes(new FirestoreCompletionHandler<List<Node>>() {
                                    @Override
                                    public void completed(List<Node> nodes) {
                                        update.setNavigationNodes(nodes);

                                        // Request opening hours.
                                        loadOpeningHours(new FirestoreCompletionHandler<List<OpeningHours>>() {
                                            @Override
                                            public void completed(List<OpeningHours> openingHours) {
                                                update.setOpeningHours(openingHours);

                                                // Request building configuration document.
                                                loadBuildingConfiguration(new FirestoreCompletionHandler<USBConfiguration>() {
                                                    @Override
                                                    public void completed(USBConfiguration usbConfiguration) {
                                                        super.completed(usbConfiguration);
                                                        update.setConfiguration(usbConfiguration);
                                                        handler.completed(update);
                                                    }

                                                    @Override
                                                    public void failed(Exception exception) {
                                                        handler.failed(exception);
                                                    }
                                                });
                                            }
                                            @Override
                                            public void failed(Exception exception) {
                                                handler.failed(exception);
                                            }
                                        });
                                    }
                                    @Override
                                    public void failed(Exception exception) {
                                        handler.failed(exception);
                                    }
                                });
                            }
                            @Override
                            public void failed(Exception exception) {
                                handler.failed(exception);
                            }
                        });
                    }
                    @Override
                    public void failed(Exception exception) {
                        handler.failed(exception);
                    }
                });
            }
            @Override
            public void failed(Exception exception) {
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads each floor in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the floors have been retrieved.
     */
    private void loadFloors(final FirestoreCompletionHandler<List<Floor>> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.FLOORS, null, new FirestoreCompletionHandler<List<Floor>>() {
            @Override
            public void completed(final List<Floor> floors) {
                // Configure completion handler for whenever a room has been retrieved.
                final FirestoreCompletionHandler<Void> roomLoadHandler = new FirestoreCompletionHandler<Void>(floors.size()) {
                    @Override
                    public void completed(Void aVoid) {
                        super.completed(aVoid);
                        if (isCompleted()) {
                            handler.completed(floors);
                        }
                    }
                    @Override
                    public void failed(Exception exception) {
                        super.failed(exception);
                        if (isCompleted()) {
                            handler.failed(exception);
                        }
                    }
                };

                // Check whether there are floors to download rooms.
                if (floors.size() == 0) {
                    handler.failed(new Exception("No floors retrieved"));
                    return;
                }

                // Load rooms on each floor.
                for (final Floor floor : floors) {
                    loadRooms(floor, roomLoadHandler);
                }
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB floors", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads each room situated on a floor in the Urban Sciences Building.
     *
     * @param floor The floor on which to load rooms.
     * @param handler The completion handler called once the floors have been retrieved.
     */
    private void loadRooms(final Floor floor, final FirestoreCompletionHandler<Void> handler) {
        String roomsPath = FirestoreDatabaseCollection.FLOORS.getCollectionIdentifier() + "/" + floor.getNumber();
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.ROOMS, roomsPath, new FirestoreCompletionHandler<List<Room>>() {
            @Override
            public void completed(List<Room> rooms) {
                for (Room room : rooms) {
                    room.attachFloor(floor);
                    floor.attachRoom(room);
                }
                handler.completed(null);
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB rooms on floor " + floor.getNumber(), exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads each staff member in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the staff members have been retrieved.
     */
    private void loadStaffMembers(final FirestoreCompletionHandler<List<StaffMember>> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.STAFF, null, new FirestoreCompletionHandler<List<StaffMember>>() {
            @Override
            public void completed(final List<StaffMember> staffMembers) {
                handler.completed(staffMembers);
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB staff members", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads each item which is served at the café in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the café menu items have been retrieved.
     */
    private void loadCafeMenuItems(final FirestoreCompletionHandler<List<CafeMenuItem>> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.CAFE_MENU, null, new FirestoreCompletionHandler<List<CafeMenuItem>>() {
            @Override
            public void completed(final List<CafeMenuItem> menuItems) {
                handler.completed(menuItems);
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB café menu items", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads the navigation nodes in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the navigation nodes have been retrieved.
     */
    private void loadNavigationNodes(final FirestoreCompletionHandler<List<Node>> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.NAVIGATION_NODES, null, new FirestoreCompletionHandler<List<Node>>() {
            @Override
            public void completed(final List<Node> nodes) {
                handler.completed(nodes);
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB navigation nodes", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads the opening hours in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the opening hours have been retrieved.
     */
    private void loadOpeningHours(final FirestoreCompletionHandler<List<OpeningHours>> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.OPENING_HOURS, null, new FirestoreCompletionHandler<List<OpeningHours>>() {
            @Override
            public void completed(final List<OpeningHours> openingHours) {
                handler.completed(openingHours);
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB opening hours", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * Loads the opening hours in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the opening hours have been retrieved.
     */
    private void loadBuildingConfiguration(final FirestoreCompletionHandler<USBConfiguration> handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.CONFIGURATION, null, new FirestoreCompletionHandler<List<USBConfiguration>>() {
            @Override
            public void completed(List<USBConfiguration> usbConfigurations) {
                super.completed(usbConfigurations);
                if (usbConfigurations.size() == 1) {
                    USBConfiguration config = usbConfigurations.get(0);
                    handler.completed(config);
                    return;
                }
                handler.failed(new Exception("A single configuration file was not found"));
            }
            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB Configuration", exception);
                handler.failed(exception);
            }
        });
    }

    /**
     * An Urban Sciences Building update completion handler.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public interface UpdateCompletionHandler {

        /**
         * Called when the Urban Sciences Building requires an update.
         *
         * @param force Boolean value whether an update needs to be completed.
         *              For example, {@code force} will be {@code true} if there is no cached version
         *              of the Urban Sciences Building available.
         */
        void requiresUpdate(boolean force);

        /**
         * Called when the Urban Sciences Building has been loaded from the cache.
         */
        void loadedFromCache();
    }
}
