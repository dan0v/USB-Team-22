package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A class which represents the Urban Science Building, stores a list of floors,
 * and deals with building state changes based on current device time.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class USBManager {
    private static USBManager sharedInstance = new USBManager();

    private List<Floor> floors = new ArrayList<>();
    private BuildingState buildingState;

    //times read from stored data
    private List<Calendar> oTimes; //opening times
    private List<Calendar> cTimes; //closing times
    private List<Calendar> oohTimes; //out of hours times

    public static USBManager getInstance()
    {
        return sharedInstance;
    }

    /**
     * Disallow multiple instances of USBManager
     */
    private USBManager() {}

    /**
     * Method to add data to USBManager instance at launch instead of constructor.
     * @param oTimes List of opening times (0 to 6) - could use arrays instead
     * @param cTimes List of closing times (0 to 6)
     * @param oohTimes List of out of hours times (0 to 6)
     * @param floors List of out of floors to be added to USBManager
     */
    public void prepareUSBManager(List<Calendar> oTimes, List<Calendar> cTimes, List<Calendar> oohTimes, List<Floor> floors) {
        this.oTimes = oTimes;
        this.cTimes = cTimes;
        this.oohTimes = oohTimes;
        this.floors = floors;

        checkBuildingState(); //check building state at application launch
    }

    public List<Floor> getFloors() {
        return this.floors;
    }

    /**
     * Compare current day and time to stored: open, closed, out of hours boundaries. To be called
     * by a service TODO create timed looping service
     * at time intervals. Calls {@link #setBuildingState(BuildingState)} if building
     * state has changed.
     */
    public void checkBuildingState() {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        double currentTime = currentCalendar.getTimeInMillis();
        int currentDay = currentCalendar.get(Calendar.DAY_OF_WEEK);

        //Saturday = 1, Monday = 2 etc.
        double oTime = this.oTimes.get(currentDay).getTimeInMillis();
        double cTime = this.cTimes.get(currentDay).getTimeInMillis();
        double oohTime = this.oohTimes.get(currentDay).getTimeInMillis();

        if(currentTime < oTime || currentTime > oohTime) {
            if(currentTime > cTime) {
                //fully closed
                if(this.buildingState != BuildingState.CLOSED) {
                    this.buildingState = BuildingState.CLOSED;
                    setBuildingState(this.buildingState);
                }
            }
            else {
                //out of hours
                if(this.buildingState != BuildingState.OUT_OF_HOURS) {
                    this.buildingState = BuildingState.OUT_OF_HOURS;
                    setBuildingState(this.buildingState);
                }
            }
        }
        else {
            //open
            if(this.buildingState != BuildingState.OPEN) {
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
}
