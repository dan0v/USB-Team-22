package uk.ac.newcastle.team22.usb.search;

import android.support.annotation.StringRes;

/**
 * A class to define search result reasons i.e. why this result has been returned
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public enum ResultReason {
    PHONE_NUMBER, STAFF, ROOM, CAFE_ITEM, RESOURCE;

    public String getReason(){
        switch(this) {
            case PHONE_NUMBER:  return "Phone number";
            case STAFF:         return "Staff member";
            case ROOM:          return "Room";
            case CAFE_ITEM:     return "Cafe Menu Item";
            case RESOURCE:      return "Room Resource";
            default:            return "";
        }
    }

}
