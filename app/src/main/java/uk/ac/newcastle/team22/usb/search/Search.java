package uk.ac.newcastle.team22.usb.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * The class which defines the search algorithm.
 *
 * @author Alexander MacLeod
 * @author Patrick Lindley
 * @version 1.0
 */
public class Search<T extends Searchable>  {

    /** The query string. */
    private String query;

    /** The type of results to display. If set to null, all results will be displayed. */
    private Class<T> resultFilter;

    public Search(String query, Class<T> resultFilter) {
        // Lowercase query string to avoid missing results.
        // Trim whitespace on either side of the query string.
        this.query = query.toLowerCase().trim();
        this.resultFilter = resultFilter;
    }

    /**
     * Gathers all objects conforming to {@link Searchable} to a list, each assigned a reason.
     * Searches all rooms, staff members, café items and resources for the query.
     * Orders results based on priority.
     *
     * @return List of search results.
     */
    public List<SearchResult> search() {
        List<SearchResult> results = new ArrayList<>();

        // Check for an empty query.
        if (query.isEmpty()) {
            return results;
        }

        // Build searchable database.
        USB building = USBManager.shared.getBuilding();
        List<Searchable> toSearch = new ArrayList<>();

        // Show room and resources first.
        for (Floor floor : building.getFloors().values()) {
            for (Room room : floor.getRooms().values()) {
                toSearch.add(room);
                toSearch.addAll(room.getResources());
            }
        }

        // Show café menu items and staff members next.
        toSearch.addAll(building.getCafe().getItems());
        toSearch.addAll(building.getStaffMembers());

        // Check each item in database for a matching query.
        for (Searchable potentialResult : toSearch) {
            // Determine whether result should be included.
            if (resultFilter != null && !potentialResult.getClass().equals(resultFilter)) {
                continue;
            }
            // Determine whether the result matches the query.
            SearchResult result = determineWhetherSearchResult(potentialResult);
            if (result != null) {
                results.add(result);
            }
        }

        // Sort search results by priority.
        Collections.sort(results, new Comparator<SearchResult>() {
            public int compare(SearchResult o1, SearchResult o2) {
                return o1.getPriority().compareTo(o2.getPriority());
            }
        });

        return results;
    }


    /**
     * Determines whether an object conforming to {@link Searchable} is a valid search result.
     *
     * @param potentialResult The object to be tested for a search result.
     * @return The search result.
     */
    private SearchResult determineWhetherSearchResult(Searchable potentialResult) {
        for (ResultReason reason : potentialResult.getSearchableReasons()) {
            // Checks if result starts with value.
            if (reason.getAttribute().toLowerCase().startsWith(query)) {
                return new SearchResult(potentialResult, Priority.HIGH, reason);
            } else if (reason.getAttribute().toLowerCase().contains(query)) {
                return new SearchResult(potentialResult, Priority.LOW, reason);
            }
        }
        return null;
    }
}
