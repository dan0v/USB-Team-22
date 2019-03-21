package uk.ac.newcastle.team22.usb.search;

/**
 * A class which represents a search result when the user uses the search feature.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class SearchResult {

    /** The object that is being searched. */
    private Searchable result;

    /** Priority of the search result. */
    private Priority priority;

    /** The reason for why the search result has been returned. */
    private ResultReason reason;

    public SearchResult(Searchable result, Priority priority, ResultReason reason) {
        this.result = result;
        this.priority = priority;
        this.reason = reason;
    }

    /**
     * @return The object which was returned.
     */
    public Searchable getResult() {
        return result;
    }

    /**
     * @return The priority of the search result.
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @return The reason for why the search result has been returned.
     */
    public ResultReason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return result.toString();
    }
}
