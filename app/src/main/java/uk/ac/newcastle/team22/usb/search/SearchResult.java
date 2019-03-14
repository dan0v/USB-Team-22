package uk.ac.newcastle.team22.usb.search;

/**
 * A class which represents a search result when the user uses the search feature.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class SearchResult {

    private Searchable result;

    private int priority;

    private ResultReason reason;

    public Searchable getResult() {
        return result;
    }

    public int getPriority() {
        return priority;
    }

    public ResultReason getReason() {
        return reason;
    }

    public SearchResult(Searchable result, int priority, ResultReason reason) {
        this.result = result;
        this.priority = priority;
        this.reason = reason;
    }
}
