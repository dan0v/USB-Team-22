package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A class which represents the Urban Sciences Building.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class USB {

    /** The floors in the Urban Sciences Building. */
    private List<Floor> floors = new ArrayList<>();

    private BuildingState buildingState;

    //times read from stored data
    private List<Calendar> oTimes; //opening times
    private List<Calendar> cTimes; //closing times
    private List<Calendar> oohTimes; //out of hours times

    /** Empty constructor. */
    public USB() {}

    /**
     * Constructor for one time construction of USBManager.
     * @param oTimes List of opening times (0 to 6) - could use arrays instead
     * @param cTimes List of closing times (0 to 6)
     * @param oohTimes List of out of hours times (0 to 6)
     * @param floors List of out of floors to be added to USBManager
     */
    private USB(List<Calendar> oTimes, List<Calendar> cTimes, List<Calendar> oohTimes, List<Floor> floors) {
        this.oTimes = oTimes;
        this.cTimes = cTimes;
        this.oohTimes = oohTimes;
        this.floors = floors;

        checkBuildingState();
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
     * @return current state of USB as enum
     */
    public BuildingState getBuildingState() {
        return this.buildingState;
    }

    /**
     * @return List of floors in USB
     */
    public List<Floor> getFloors() {
        return this.floors;
    }

    /**
     * Sets the floors in the building.
     * @param floors The floors in the building.
     */
    public void setFloors(List<Floor> floors) {
        this.floors = floors;
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
