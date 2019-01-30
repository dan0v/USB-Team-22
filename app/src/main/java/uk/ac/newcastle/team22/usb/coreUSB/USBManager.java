package uk.ac.newcastle.team22.usb.coreUSB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class USBManager {
    private List<Floor> floors = new ArrayList<>();
    private boolean outOfHours = false;

    public Calendar oTimeD1; //opening and closing times for each day of the week
    private Calendar cTimeD1;
    private Calendar oTimeD2;
    private Calendar cTimeD2;
    private Calendar oTimeD3;
    private Calendar cTimeD3;
    private Calendar oTimeD4;
    private Calendar cTimeD4;
    private Calendar oTimeD5;
    private Calendar cTimeD5;
    private Calendar oTimeD6;
    private Calendar cTimeD6;
    private Calendar oTimeD7;
    private Calendar cTimeD7;

    private List<Calendar> oAndCTimes;

    public static USBManager shared = new USBManager();

    public USBManager(){

    }

    public List<Floor> getFloors() {
        return this.floors;
    }

    public void checkOpenStatus(){
//        Calendar currentTime = Calendar.getInstance(Locale.getDefault());
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);
    }

}
