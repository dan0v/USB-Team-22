package uk.ac.newcastle.team22.usb.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.CafeMenuItem;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USB;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * The class from which the search algorithm is called.
 * Searches through all fields in staff, rooms, cafe menu, room resources
 * Each object that is found to match will have a copy created, assigned an enum for reason, and a priority based on % string match to the field
 * This SearchResult object will be placed in Search class map, which will be returned to display the results.
 */
public class Search {

    private String query;

    public Search(String query) {
        this.query = query;
    }

    //actual search algorithm
    public List<SearchResult> search() {
        List<SearchResult> results = new ArrayList<>();

        USB building = USBManager.shared.getBuilding();
        List<Searchable> toSearch = new ArrayList<>();

        // Determine objects to search.
        // Add staff members, cafe items.
        toSearch.addAll(building.getCafe().getItems());
        toSearch.addAll(building.getStaffMembers());

        // Add all floors, rooms and resources.
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

        return results;
    }


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
