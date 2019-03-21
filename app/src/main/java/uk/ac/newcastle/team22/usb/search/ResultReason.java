package uk.ac.newcastle.team22.usb.search;

import android.support.annotation.StringRes;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class to define search result reasons i.e. why this result has been returned
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class ResultReason {

    /** The field value of the object being searched. */
    private String attribute;

    /** The Enum reason for the result being found. */
    private Reason reason;

    /** Constructor for ResultReason */
    public ResultReason(String attribute, Reason reason) {
        this.attribute = attribute;
        this.reason = reason;
    }


    public enum Reason {
        PHONE_NUMBER, STAFF, ROOM, CAFE_ITEM_NAME, RESOURCE, EMAIL;

        /**
         * @return Localised String representation of a ResultReason.
         */
        public @StringRes int getLocalisedDirection() {
            switch(this) {
                case PHONE_NUMBER:  return R.string.reasonPhoneNumber;
                case STAFF:         return R.string.reasonStaffMember;
                case ROOM:          return R.string.reasonRoom;
                case CAFE_ITEM_NAME:     return R.string.reasonCafeMenuItem;
                case RESOURCE:      return R.string.reasonResource;
                case EMAIL:         return R.string.reasonEmail;
                default:            return 0;
            }
        }
    }

    /** @return the object attribute */
    public String getAttribute() {
        return attribute;
    }

    /** @return the reason for being a result */
    public Reason getReason() {
        return reason;
    }
}
