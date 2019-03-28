package uk.ac.newcastle.team22.usb.search;

import java.util.Comparator;

/**
 * An enum which defines the priority of a {@link SearchResult}.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public enum Priority implements Comparator<Priority> {
    LOW, HIGH;

    /**
     * @return The integer value of the priority.
     */
    private int getIntegerValue() {
        switch (this) {
            case LOW:   return 0;
            case HIGH:  return 1;
            default:    return -1;
        }
    }

    @Override
    public int compare(Priority o1, Priority o2) {
        // -1 less than; 1 greater than; 0 equal.
        if (o1.getIntegerValue() == o2.getIntegerValue()) {
            return 0;
        }
        return o1.getIntegerValue() < o2.getIntegerValue() ? -1 : 1;
    }
}
