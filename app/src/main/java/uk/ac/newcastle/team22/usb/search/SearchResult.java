package uk.ac.newcastle.team22.usb.search;

/**
 * A class which represents a search result when the user uses the search feature.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public class SearchResult {

    private Object result;
    private int priority;
    private Enum reason;

    public Object getResult() {
        return result;
    }
    public int getPriority() {
        return priority;
    }
    public Enum getReason() {
        return reason;
    }

    public SearchResult(Object result, int priority, Enum reason) {
        this.result = result;
        this.priority = priority;
        this.reason = reason;
    }
}
