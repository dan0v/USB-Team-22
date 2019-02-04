package uk.ac.newcastle.team22.usb.coreUSB;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import uk.ac.newcastle.team22.usb.firebase.*;

/**
 * The manager for the maintenance of Urban Sciences Building data.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USBUpdateManager {

    /** The exception to throw when a cached version of the Urban Sciences Building is not available. */
    public class USBNoCachedVersionAvailable extends Exception {}

    /**
     * Requests a cached version of the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been retrieved.
     */
    public void requestCached(final FirestoreCompletionHandler<USBUpdate> handler) {
        // Disable network access to force application to load cached data, if available.
        FirebaseManager.shared.enableCache(new FirestoreCompletionHandler<Void>() {
            @Override
            public void completed(Void aVoid) {
                super.completed(aVoid);

                // Request the cached version of the Urban Sciences Building.
                update(new FirestoreCompletionHandler<USBUpdate>() {
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
     * Updates the floors and rooms in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been updated.
     */
    public void update(final FirestoreCompletionHandler<USBUpdate> handler) {
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

    /**
     * An Urban Sciences Building update.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public class USBUpdate {

        /** The floors in the Urban Sciences Building. */
        private List<Floor> floors = new ArrayList<>();

        /** The staff members in the Urban Sciences Building. */
        private List<StaffMember> staffMembers = new ArrayList<>();

        /** The items, food or drink, which are served at the café in the Urban Sciences Building. */
        private List<CafeMenuItem> cafeMenuItems = new ArrayList<>();

        /** Empty constructor. */
        USBUpdate() {}

        /**
         * Sets the new floors in the update.
         * @param floors The updated floors.
         */
        public void setFloors(List<Floor> floors) {
            this.floors = floors;
        }

        /**
         * Sets the new staff members in the update.
         * @param staffMembers The updated floors.
         */
        public void setStaffMembers(List<StaffMember> staffMembers) {
            this.staffMembers = staffMembers;
        }

        /**
         * Sets the new café menu items in the update.
         * @param cafeMenuItems The updated café menu items.
         */
        public void setCafeMenuItems(List<CafeMenuItem> cafeMenuItems) {
            this.cafeMenuItems = cafeMenuItems;
        }

        /**
         * @return The updated floors.
         */
        public List<Floor> getFloors() {
            return floors;
        }

        /**
         * @return The updated staff members.
         */
        public List<StaffMember> getStaffMembers() {
            return staffMembers;
        }

        /**
         * @return The updated café menu items.
         */
        public List<CafeMenuItem> getCafeMenuItems() {
            return cafeMenuItems;
        }
    }
}
