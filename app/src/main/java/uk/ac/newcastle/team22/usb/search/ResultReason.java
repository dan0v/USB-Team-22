package uk.ac.newcastle.team22.usb.search;

/**
 * A class to define search result reasons i.e. why this result has been returned
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class ResultReason {

    private String attribute;

    private Reason reason;

    public ResultReason(String attribute, Reason reason) {
        this.attribute = attribute;
        this.reason = reason;
    }

    public enum Reason {
        PHONE_NUMBER, STAFF, ROOM, CAFE_ITEM_NAME, RESOURCE, EMAIL;

        public String getReason(){
            switch(this) {
                case PHONE_NUMBER:  return "Phone number";
                case STAFF:         return "Staff member";
                case ROOM:          return "Room";
                case CAFE_ITEM_NAME:     return "Cafe Menu Item Name";
                case RESOURCE:      return "Room Resource";
                case EMAIL:         return "email address";
                default:            return "";
            }
        }
    }

    public String getAttribute() {
        return attribute;
    }

    public Reason getReason() {
        return reason;
    }
}
