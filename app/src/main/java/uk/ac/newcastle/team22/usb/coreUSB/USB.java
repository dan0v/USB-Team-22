package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A class which represents the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USB {

    /** The floors in the Urban Sciences Building. */
    private List<Floor> floors;

    /** The staff members in the Urban Sciences Building. */
    private List<StaffMember> staffMembers;

    /** The café in the Urban Sciences Building. */
    private Cafe cafe;

    private BuildingState buildingState;

    public List<Node> sharedNavNodes;

    //times read from stored data
    private List<Calendar> oTimes; //opening times
    private List<Calendar> cTimes; //closing times
    private List<Calendar> oohTimes; //out of hours times

    /**
     * Constructor using a {@link USBUpdateManager.USBUpdate}.
     * This constructor is used to initialise a {@link USB} from either a cached version of the
     * building or from new data retrieved from Firestore. Both new and cached versions of the
     * building are represented by {@link USBUpdateManager.USBUpdate}. This constructor will usually be called at
     * application launch by {@link USBManager } to initialise the shared {@link USB} instance.
     *
     * See {@link USBUpdateManager.USBUpdate} for more information on Urban Sciences Building
     * updates.
     *
     * @param update The Urban Sciences Building update.
     */
    USB(USBUpdateManager.USBUpdate update) {
        this.floors = update.getFloors();
        this.staffMembers = update.getStaffMembers();
        this.cafe = new Cafe(update);
    }

    /**
     * Compare current day and time to stored: open, closed, out of hours boundaries. To be called
     * by a service TODO create timed looping service
     * at time intervals. Calls {@link #setBuildingState(BuildingState)} if building
     * state has changed.
     */
    private void checkBuildingState() {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        double currentTime = currentCalendar.getTimeInMillis();
        int currentDay = currentCalendar.get(Calendar.DAY_OF_WEEK);

        // Saturday = 1, Monday = 2 etc.
        double oTime = this.oTimes.get(currentDay).getTimeInMillis();
        double cTime = this.cTimes.get(currentDay).getTimeInMillis();
        double oohTime = this.oohTimes.get(currentDay).getTimeInMillis();

        if(currentTime < oTime || currentTime > oohTime) {
            if (currentTime > cTime) {
                //fully closed
                if(this.buildingState != BuildingState.CLOSED) {
                    this.buildingState = BuildingState.CLOSED;
                    setBuildingState(this.buildingState);
                }
            } else {
                //out of hours
                if (this.buildingState != BuildingState.OUT_OF_HOURS) {
                    this.buildingState = BuildingState.OUT_OF_HOURS;
                    setBuildingState(this.buildingState);
                }
            }
        } else {
            if (this.buildingState != BuildingState.OPEN) {
                this.buildingState = BuildingState.OPEN;
                setBuildingState(this.buildingState);
            }
        }
    }

    /**
     * Set building state according to parameter and update UI elements, route planning accordingly
     * @param newBuildingState enum denotes state building should now be treated as
     */
    private void setBuildingState(BuildingState newBuildingState) {
        //TODO display building state change on UI etc.
    }

    /**
     * @return The floors in the Urban Sciences Building.
     */
    public List<Floor> getFloors() {
        return this.floors;
    }

    /**
     * @return The staff members in the Urban Sciences Building.
     */
    public List<StaffMember> getStaffMembers() {
        return staffMembers;
    }

    /**
     * @return The café at the Urban Sciences Building.
     */
    public Cafe getCafe() {
        return cafe;
    }

    /**
     * @return The current state of the Urban Sciences Building.
     */
    public BuildingState getBuildingState() {
        return this.buildingState;
    }

    @Override
    public String toString() {
        int roomsCount = 0;
        for (Floor floor : floors) {
            roomsCount += floor.getRooms().size();
        }
        return "USB (floors: " + floors.size() + ", rooms: " + roomsCount + ")";
    }
}
