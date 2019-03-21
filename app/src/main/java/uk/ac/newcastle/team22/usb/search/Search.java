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
 * The class from which the search algorithm is called.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public class Search {

    /** The string query being searched for. */
    private String query;

    /** Constructor for Search */
    public Search(String query) {
        this.query = query;
    }

    /**
     * Adds all Searchable objects to a list, each assigned a Reason.
     * Searches all rooms, staff, cafe items and resources for the query.
     * Orders results based on priority, 0 being highest and 2 being lowest.
     * @return List of SearchResults
     */
    public List<SearchResult> search() {
        List<SearchResult> results = new ArrayList<>();

        USB building = USBManager.shared.getBuilding();
        List<Searchable> toSearch = new ArrayList<>();

        toSearch.addAll(building.getCafe().getItems());
        toSearch.addAll(building.getStaffMembers());

        for (Floor floor : building.getFloors()) {
            for (Room room : floor.getRooms()) {
                toSearch.add(room);
                toSearch.addAll(room.getResources());
            }
        }

        for (Searchable potentialResult : toSearch) {
            SearchResult result = determineWhetherSearchResult(potentialResult);
            if (result != null) {
                results.add(result);
            }
        }

        Collections.sort(results, new Comparator<SearchResult>(){
            public int compare(SearchResult o1, SearchResult o2){
                if(o1.getPriority() == o2.getPriority())
                    return 0;
                return o1.getPriority() < o2.getPriority() ? -1 : 1;
            }
        });

        return results;
    }


    /**
     * Compares the object filed to the search query to determine if a result.
     * @param potentialResult Object filed to be searched.
     * @return SearchResult found.
     */
    private SearchResult determineWhetherSearchResult(Searchable potentialResult) {
        SearchResult result = null;

        for (ResultReason reason : potentialResult.getSearchableReasons()) {
            //checks if result starts with value
            if (reason.getAttribute().startsWith(query)) {
                result = new SearchResult(potentialResult, 0, reason);
            } else if (reason.getAttribute().contains(query)) {
                result = new SearchResult(potentialResult, 1, reason);
            }
        }
        return result;
    }
}
