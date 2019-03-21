package uk.ac.newcastle.team22.usb.search;

/**
 * A class which represents a search result when the user uses the search feature.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class SearchResult {

    /** The object that is being searched */
    private Searchable result;

    /** Priority of the search result; 0 highest, 2 lowest */
    private int priority;

    /** Enum for why that search result has been selected */
    private ResultReason reason;

    /**
     * @return The result
     */
    public Searchable getResult() {
        return result;
    }

    /**
     * @return The priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @return The reason
     */
    public ResultReason getReason() {
        return reason;
    }

    /**
     * Constructor for SearchResult.
     * @param result The searchable object.
     * @param priority The priority of the SearchResult
     * @param reason The reason for the SearchResult being returned.
     */
    public SearchResult(Searchable result, int priority, ResultReason reason) {
        this.result = result;
        this.priority = priority;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return result.toString();
    }
}
