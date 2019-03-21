package uk.ac.newcastle.team22.usb.search;

import android.support.annotation.StringRes;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class which defines a search result reason.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class ResultReason {

    /** The field value of the object being searched. */
    private String attribute;

    /** The reason for the result being found. */
    private Reason reason;

    public ResultReason(String attribute, Reason reason) {
        this.attribute = attribute;
        this.reason = reason;
    }

    /** The category of search result. */
    public enum Reason {
        PHONE_NUMBER, STAFF, ROOM, CAFE_ITEM_NAME, RESOURCE, EMAIL;

        /**
         * @return Localised string representation of a {@link ResultReason}.
         */
        public @StringRes int getLocalisedDirection() {
            switch(this) {
                case PHONE_NUMBER:      return R.string.reasonPhoneNumber;
                case STAFF:             return R.string.reasonStaffMember;
                case ROOM:              return R.string.reasonRoom;
                case CAFE_ITEM_NAME:    return R.string.reasonCafeMenuItem;
                case RESOURCE:          return R.string.reasonResource;
                case EMAIL:             return R.string.reasonEmail;
                default:                return 0;
            }
        }
    }

    /**
     * @return The object which was returned as a result.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @return The reason of the search result.
     */
    public Reason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return reason.toString();
    }
}
