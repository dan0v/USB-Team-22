package uk.ac.newcastle.team22.usb.coreUSB;

import android.util.Log;

import java.util.List;
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
    public void requestCached(final FirestoreCompletionHandler handler) {
        // TODO Check for cached version of USB.
        boolean cacheAvailable = false;
        if (!cacheAvailable) {
            handler.failed(new USBNoCachedVersionAvailable());
            return;
        }
    }

    /**
     * Updates the floors and rooms in the Urban Sciences Building.
     *
     * @param handler The completion handler called once the Urban Sciences Building has been updated.
     */
    public void update(final FirestoreCompletionHandler handler) {
        final USB newBuilding = new USB();

        // Request updated floors.
        loadFloors(new FirestoreCompletionHandler<List<Floor>>() {
            @Override
            public void completed(List<Floor> floors) {
                newBuilding.setFloors(floors);
                handler.completed(newBuilding);
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
    private void loadFloors(final FirestoreCompletionHandler handler) {
        FirebaseManager.shared.getDocuments(FirestoreDatabaseCollection.FLOORS, null, new FirestoreCompletionHandler<List<Floor>>() {
            @Override
            public void completed(final List<Floor> floors) {
                for (final Floor floor : floors) {
                    loadRooms(floor, new FirestoreCompletionHandler<Void>() {
                        @Override
                        public void completed(Void aVoid) {
                            checkForCompletion();
                        }

                        @Override
                        public void failed(Exception exception) {
                            checkForCompletion();
                        }

                        // TODO Hacky method for now. Proof of concept.
                        private void checkForCompletion() {
                            boolean completed = true;
                            for (Floor floor : floors) {
                                if (floor.getRooms().size() == 0) {
                                    completed = false;
                                }
                            }
                            if (completed) {
                                handler.completed(floors);
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Exception exception) {
                Log.e("", "Unable to retrieve USB floors", exception);
            }
        });
    }

    /**
     * Loads each room situated on a floor in the Urban Sciences Building.
     *
     * @param floor The floor on which to load rooms.
     * @param handler The completion handler called once the floors have been retrieved.
     */
    private void loadRooms(final Floor floor, final FirestoreCompletionHandler handler) {
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
